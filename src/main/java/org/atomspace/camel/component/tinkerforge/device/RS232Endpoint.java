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
package org.atomspace.camel.component.tinkerforge.device;

import org.apache.camel.Consumer;
import org.apache.camel.Endpoint;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.atomspace.camel.component.tinkerforge.TinkerforgeComponent;
import org.atomspace.camel.component.tinkerforge.TinkerforgeEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.BrickletRS232;

public class RS232Endpoint extends TinkerforgeEndpoint<RS232Consumer, RS232Producer> {

    private static final Logger LOG = LoggerFactory.getLogger(RS232Endpoint.class);
    
    private char[] message;
    private Short length;
    private Short baudrate;
    private Short parity;
    private Short stopbits;
    private Short wordlength;
    private Short hardwareFlowcontrol;
    private Short softwareFlowcontrol;

        
    public RS232Endpoint(String uri, TinkerforgeComponent tinkerforgeComponent) {
        super(uri, tinkerforgeComponent);
    }

    @Override
    public Producer createProducer() throws Exception {
        LOG.trace("createProducer()");
        if(producer==null){
            producer = new RS232Producer(this);
        }
        return producer;
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        LOG.trace("createConsumer(Processor processor='"+processor+"')");
        if(consumer==null){
            consumer = new RS232Consumer(this, processor);
        }
        return consumer;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
    
    public void init(BrickletRS232 device) throws Exception {
        if(getInit()==null) return;
        
        String[] initFunctions = getInit().split(",");
        for (String function : initFunctions) {
            callFunction(device, function, null, this);
        }
    }
    
    public Object callFunction(BrickletRS232 device, String function, Message m, Endpoint e) throws Exception{
        Object response = null;
        switch (function) {
                
            case "write":
                response = device.write(
                        getValue(char[].class, "message", m, getMessage()),
                        getValue(short.class, "length", m, getLength())
                    );
                break;

            case "read":
                response = device.read();
                break;

            case "enableReadCallback":
                device.enableReadCallback();
                break;

            case "disableReadCallback":
                device.disableReadCallback();
                break;

            case "isReadCallbackEnabled":
                response = device.isReadCallbackEnabled();
                break;

            case "setConfiguration":
                device.setConfiguration(
                        getValue(short.class, "baudrate", m, getBaudrate()),
                        getValue(short.class, "parity", m, getParity()),
                        getValue(short.class, "stopbits", m, getStopbits()),
                        getValue(short.class, "wordlength", m, getWordlength()),
                        getValue(short.class, "hardwareFlowcontrol", m, getHardwareFlowcontrol()),
                        getValue(short.class, "softwareFlowcontrol", m, getSoftwareFlowcontrol())
                    );
                break;

            case "getConfiguration":
                response = device.getConfiguration();
                break;

            case "getIdentity":
                response = device.getIdentity();
                break;


            default: throw new Exception("unknown function '"+function+"'");
            
        }
        
        return response;
    }
    
    
    public char[] getMessage(){
        return message;
    }

    public void setMessage(char[] message){
        this.message = message;
    }

    public Short getLength(){
        return length;
    }

    public void setLength(Short length){
        this.length = length;
    }

    public Short getBaudrate(){
        return baudrate;
    }

    public void setBaudrate(Short baudrate){
        this.baudrate = baudrate;
    }

    public Short getParity(){
        return parity;
    }

    public void setParity(Short parity){
        this.parity = parity;
    }

    public Short getStopbits(){
        return stopbits;
    }

    public void setStopbits(Short stopbits){
        this.stopbits = stopbits;
    }

    public Short getWordlength(){
        return wordlength;
    }

    public void setWordlength(Short wordlength){
        this.wordlength = wordlength;
    }

    public Short getHardwareFlowcontrol(){
        return hardwareFlowcontrol;
    }

    public void setHardwareFlowcontrol(Short hardwareFlowcontrol){
        this.hardwareFlowcontrol = hardwareFlowcontrol;
    }

    public Short getSoftwareFlowcontrol(){
        return softwareFlowcontrol;
    }

    public void setSoftwareFlowcontrol(Short softwareFlowcontrol){
        this.softwareFlowcontrol = softwareFlowcontrol;
    }



}