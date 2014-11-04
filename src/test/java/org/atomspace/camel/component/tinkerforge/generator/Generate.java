/**
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements. See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.atomspace.camel.component.tinkerforge.generator;

import java.beans.Introspector;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.camel.SuspendableService;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.Log4JLogChute;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.atomspace.camel.component.tinkerforge.generator.model.Config;
import org.atomspace.camel.component.tinkerforge.generator.model.Packet;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Generate {
    
    private static final Logger LOG = Logger.getLogger(Generate.class.getName());
    private ObjectMapper mapper = new ObjectMapper();
    
    public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
        System.out.println("GENERATE SOURCES");
        new Generate().generate();
    }
    
    public Generate() { }
    
    public void generate() throws JsonParseException, JsonMappingException, IOException {
        // INIT VELOCITY ENGINE
        VelocityEngine engine;
        Properties velocityProperties = new Properties();
        velocityProperties.setProperty(RuntimeConstants.RESOURCE_LOADER, "cloader");
        velocityProperties.setProperty("cloader.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityProperties.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, Log4JLogChute.class.getName());
        velocityProperties.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM + ".log4j.logger", LOG.getName());
        engine = new VelocityEngine(velocityProperties);
        engine.init();
        Template endpointTemplate = engine.getTemplate("endpoint.vm");
        Template consumerTemplate = engine.getTemplate("consumer.vm");
        Template consumerCallbackImplTemplate = engine.getTemplate("consumer_callback_impl.vm");
        Template producerTemplate = engine.getTemplate("producer.vm");
        
        
        
        //createClassBundle("device2", new File("src/test/resources/config/binding/bricklet_io4_config.js"), endpointTemplate, consumerTemplate, producerTemplate, consumerCallbackImplTemplate) ;
        File[] configFiles = new File("src/test/resources/config/binding").listFiles();
        for (File file : configFiles) {
            if(file.toString().endsWith("brick_red_config.json") || file.toString().endsWith("bricklet_heart_rate_config.json") ){
                
            }else if(file.toString().endsWith("json")){
                createClassBundle("device2", file, endpointTemplate, consumerTemplate, producerTemplate, consumerCallbackImplTemplate) ;
            }
        }
        
        
    }
    
    /**
     * Generate Class Bundle
     */
    private void createClassBundle(String namespace, File configFile, Template endpointTemplate,Template consumerTemplate,Template producerTemplate, Template consumerCallbackImplTemplate) throws IOException {
        InputStream inputStream2 = new FileInputStream(configFile);
        Config config = mapper.readValue(inputStream2 , Config.class);
        createEndpointClass(namespace, config, endpointTemplate, new File("src/main/java/org/atomspace/camel/component/tinkerforge/device2/"+config.name[0]+"Endpoint.java"));
        createConsumerClass(namespace, config, consumerTemplate, new File("src/main/java/org/atomspace/camel/component/tinkerforge/device2/"+config.name[0]+"Consumer.java"), consumerCallbackImplTemplate);
        createProducerClass(namespace, config, producerTemplate, new File("src/main/java/org/atomspace/camel/component/tinkerforge/device2/"+config.name[0]+"Producer.java"));
        
    }
    
    /**
     * Generate Producer Class
     */
    private void createProducerClass(String namespace, Config config, Template template, File file) throws IOException {
        // MAP TINKERFORGE-CONFIG WITH VELOCITY-CONTEXT 
        VelocityContext context = new VelocityContext();
        context.put("namespace", namespace);
        context.put("config_name_0", config.name[0]);
        context.put("config_category", config.category);
        
        //TODO IMPL Tinkerforge FUNCTIONs


        // 
        BufferedWriter writer =  new BufferedWriter(new FileWriter(file)); 
        template.merge(context, writer);
        writer.close();
        //System.out.println(writer.toString());
    }
    
    /**
     * Generate Consumer Class
     */
    private void createConsumerClass(String namespace, Config config, Template template, File file, Template subTemplate) throws IOException {
        // MAP TINKERFORGE-CONFIG WITH VELOCITY-CONTEXT 
        VelocityContext context = new VelocityContext();
        context.put("namespace", namespace);
        context.put("config_name_0", config.name[0]);
        context.put("config_category", config.category);
        
        String importListener = "";
        String implementsListener = null;
        String registerCallbackAll="";
        String registerCallbackIfs="";
        String callbackImpls="";
        for (Packet packet : config.packets) {
            System.out.println(packet.name[0]);
            if(packet.type.equals("callback")){
                importListener+= "\nimport com.tinkerforge."+config.category+config.name[0]+"."+packet.name[0]+"Listener;";
                
                if(implementsListener==null) implementsListener = "implements "+packet.name[0]+"Listener";
                else implementsListener+= ", "+packet.name[0]+"Listener";
                
                registerCallbackAll+= "device.add"+packet.name[0]+"Listener(this);\n            ";
                
                registerCallbackIfs+= "if(callback.equals(\""+packet.name[0]+"Listener\")) device.add"+packet.name[0]+"Listener(this);\n                ";
                
                callbackImpls+=createCallbackImpl(config, subTemplate, packet);
                        
            }
        }
        context.put("config_packet_implements_listener", implementsListener);
        context.put("config_packet_import_listener", importListener);
        context.put("config_packet_callback_all", registerCallbackAll);
        context.put("config_packet_callback_ifs", registerCallbackIfs);
        context.put("config_packet_callback_impls", callbackImpls);
        
        
        // 
        BufferedWriter writer =  new BufferedWriter(new FileWriter(file)); 
        template.merge(context, writer);
        writer.close();
        //System.out.println(writer.toString());
    }

    /**
     * Generate Consumer Callback Impl Block
     */
    private String createCallbackImpl(Config config, Template subTemplate, Packet packet) {
        // FIND CALLBACK PARAMETERs
        String methodParameters = null;
        String headerValues = "";
        for (JsonNode element : packet.elements) {
            //1. DEEP
            if(element.size()==4){ // SIMPLE TYPE LIKE [ "period", "uint32", 1,"in"]
                //2. DEEP
                String name = element.get(0).asText();
                String type = element.get(1).asText();
                String num = element.get(2).asText(); //FOR WHAT???
                String inout = element.get(3).asText();
                
                if(inout.equals("out")){
                    // METHOD PARAMETERS
                    if(methodParameters==null) methodParameters=javaType(type)+" "+name;
                    else  methodParameters+=", "+javaType(type)+" "+name;

                    // HEADER VALUES
                    headerValues+="exchange.getIn().setHeader(\""+name+"\", "+name+");\n            ";
                    
                }
            }else if(element.size()==5){ // MORE COMPLEX TYPE LIKE ["button_r","uint8",1,"out",["ButtonState","button_state",[["Pressed","pressed",0],["Released","released",1]]]]
                //2. DEEP
                String name = element.get(0).asText();
                String type = element.get(1).asText();
                String num = element.get(2).asText(); //FOR WHAT???
                String inout = element.get(3).asText();
                
                if(inout.equals("out")){
                    // METHOD PARAMETERS
                    if(methodParameters==null) methodParameters=javaType(type)+" "+name;
                    else  methodParameters+=", "+javaType(type)+" "+name;

                    // HEADER VALUES
                    headerValues+="exchange.getIn().setHeader(\""+name+"\", "+name+");\n            ";
                    
                }                
            }else{
                System.err.println("createEndpointClass unknown element structure: "+element);
            }
            
        }
        
        
        VelocityContext subContext = new VelocityContext();       
        subContext.put("methodName", Introspector.decapitalize(packet.name[0]));
        subContext.put("methodParameters", methodParameters);
        subContext.put("headerCallbackValue", config.category+config.name[0]+".CALLBACK_"+packet.name[1].toUpperCase());
        subContext.put("headerValues", headerValues);
        subContext.put("bodyCallbackValue", packet.name[1]);
        StringWriter writer = new StringWriter();
        subTemplate.merge(subContext, writer);
        String implMeth = writer.toString();
        return implMeth;
    }

    /**
     * Generate Endpoint Class
     */
    private void createEndpointClass(String namespace, Config config, Template template, File file) throws IOException {
        // MAP TINKERFORGE-CONFIG WITH VELOCITY-CONTEXT 
        VelocityContext context = new VelocityContext();
        context.put("namespace", namespace);
        context.put("config_name_0", config.name[0]);
        context.put("config_category", config.category);
        
        StringBuffer parameters = new StringBuffer();
        StringBuffer parameterGetteSetters = new StringBuffer();
        Map<String, String> parameterSet = new HashMap<String, String>();
        StringBuffer callFunctions = new StringBuffer();
        // Find possible transfer parameters. 
        for (Packet packet : config.packets) {
            //System.out.println(packet.elements);
            
            if(packet.type.equals("function") 
                    && !packet.name[1].equals("get_response_expected") 
                    && !packet.name[1].equals("set_response_expected")
                    && !packet.name[1].equals("set_response_expected_all") 
                    && !packet.name[1].equals("get_api_version")){
            
                boolean out=false;
                boolean first=true;
                String callFunctionPre = "            case \""+camelCaseName(packet.name[1])+"\":\n";
                String callFunction = "device."+camelCaseName(packet.name[1])+"(";
                for (JsonNode element : packet.elements) {
                    
                    if(element.size()==4 || element.size()==5 ){ // SIMPLE TYPE LIKE [["relay1","bool",1,"in"]
                        String name = element.get(0).asText();
                        String type = element.get(1).asText();
                        String num = element.get(2).asText(); //FOR WHAT???
                        String inout = element.get(3).asText();
                        
                        if(inout.equals("in")){
                            if(parameterSet.get(name)==null){
                                parameterSet.put(name, name);
                                
                                parameters.append("    private "+javaBigType(type)+ " " +camelCaseName(name)+";\n");
                                
                                parameterGetteSetters.append("    public "+javaBigType(type)+ " " +camelCaseMethodeNameGet(name)+"(){\n");
                                parameterGetteSetters.append("        return "+camelCaseName(name)+";\n");
                                parameterGetteSetters.append("    }\n\n");
                                parameterGetteSetters.append("    public void "+camelCaseMethodeNameSet(name)+"("+javaBigType(type)+ " " +camelCaseName(name)+"){\n");
                                parameterGetteSetters.append("        this."+camelCaseName(name)+" = "+camelCaseName(name)+";\n");
                                parameterGetteSetters.append("    }\n\n");
                            }
                            
                            if(first){
                                callFunction += "\n                        ("+javaType(type)+") getValue(\"" +camelCaseName(name)+"\", m, e)";
                                first=false;
                            }else{
                                callFunction += ",\n                        ("+javaType(type)+") getValue(\"" +camelCaseName(name)+"\", m, e)";
                            }
                            
                        }else if(inout.equals("out")){
                            out = true;
                        }
                            
                    }else{
                        System.err.println("createEndpointClass unknown element structure: "+element);
                    }
                }
                if(first)callFunction+= ");\n";
                else callFunction+= "\n                    );\n";
                
                callFunction+= "                break;\n\n";
                
                if(out==true){ 
                    callFunction = callFunctionPre+ "                response = " + callFunction;
                }else{
                    callFunction = callFunctionPre+ "                " + callFunction;
                }
                
                callFunctions.append(callFunction);
            }
            
        }
        context.put("parameters", parameters);
        context.put("parameterGetteSetters", parameterGetteSetters);
        context.put("callFunctions", callFunctions);
        
        // WRITE FILE
        BufferedWriter writer =  new BufferedWriter(new FileWriter(file)); 
        template.merge(context, writer);
        writer.close();
        //System.out.println(writer.toString());
    }
    
    /**
     * mapping to simple Java Type
     */
    private String javaType(String type){
        if(type.equals("int8")) return "byte";
        if(type.equals("uint8")) return "short";
        if(type.equals("int16")) return "short";
        if(type.equals("uint16")) return "int";
        if(type.equals("int32")) return "int";
        if(type.equals("uint32")) return "long";
        if(type.equals("int64")) return "long";
        if(type.equals("uint64")) return "long";
        if(type.equals("float")) return "float";
        if(type.equals("bool")) return "boolean";
        if(type.equals("char")) return "char";
        if(type.equals("string")) return "String";
        return "";
    }

    /**
     * mapping to big Java Type, they can be null
     */
    private String javaBigType(String type){
        if(type.equals("int8")) return "Byte";
        if(type.equals("uint8")) return "Short";
        if(type.equals("int16")) return "Short";
        if(type.equals("uint16")) return "Integer";
        if(type.equals("int32")) return "Integer";
        if(type.equals("uint32")) return "Long";
        if(type.equals("int64")) return "Long";
        if(type.equals("uint64")) return "Long";
        if(type.equals("float")) return "Float";
        if(type.equals("bool")) return "Boolean";
        if(type.equals("char")) return "Character";
        if(type.equals("string")) return "String";
        return "";
    }
    
    /**
     * Convert 'response_expected' to 'responseExpected'
     */
    private String camelCaseName(String name){
        String camelCaseName = null;
        String[] parts = name.split("_");
        for (String part : parts) {
            if(camelCaseName==null) camelCaseName=part.toLowerCase();
            else camelCaseName+=part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase();
        }
        return camelCaseName;
    }
    
    /**
     * Convert 'response_expected' to 'getResponseExpected'
     */
    private String camelCaseMethodeNameGet(String name){
        String camelCaseName = "get";
        String[] parts = name.split("_");
        for (String part : parts) {
            camelCaseName+=part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase();
        }
        return camelCaseName;
    }
    
    /**
     * Convert 'response_expected' to 'setResponseExpected'
     */
    private String camelCaseMethodeNameSet(String name){
        String camelCaseName = "set";
        String[] parts = name.split("_");
        for (String part : parts) {
            camelCaseName+=part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase();
        }
        return camelCaseName;
    }
    
}
