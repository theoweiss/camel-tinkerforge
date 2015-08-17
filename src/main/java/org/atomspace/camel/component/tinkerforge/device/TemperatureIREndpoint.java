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

import com.tinkerforge.BrickletTemperatureIR;

/**
 * Measures contactless object temperature between -70°C and +380°C
 */
public class TemperatureIREndpoint extends TinkerforgeEndpoint<TemperatureIRConsumer, TemperatureIRProducer> {

    private static final Logger LOG = LoggerFactory.getLogger(TemperatureIREndpoint.class);
    
    private Integer emissivity;
    private Long period;
    private Long period2;
    private Character option;
    private Short min;
    private Short max;
    private Character option2;
    private Short min2;
    private Short max2;
    private Long debounce;

        
    public TemperatureIREndpoint(String uri, TinkerforgeComponent tinkerforgeComponent) {
        super(uri, tinkerforgeComponent);
    }

    @Override
    public Producer createProducer() throws Exception {
        LOG.trace("createProducer()");
        if(producer==null){
            producer = new TemperatureIRProducer(this);
        }
        return producer;
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        LOG.trace("createConsumer(Processor processor='"+processor+"')");
        if(consumer==null){
            consumer = new TemperatureIRConsumer(this, processor);
        }
        return consumer;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
    
    public void init(BrickletTemperatureIR device) throws Exception {
        if(getInit()==null) return;
        
        String[] initFunctions = getInit().split(",");
        for (String function : initFunctions) {
            callFunction(device, function, null, this);
        }
    }
    
    public Object callFunction(BrickletTemperatureIR device, String function, Message m, Endpoint e) throws Exception{
        Object response = null;
        switch (function) {
                
            case "getAmbientTemperature":
                response = device.getAmbientTemperature();
                break;

            case "getObjectTemperature":
                response = device.getObjectTemperature();
                break;

            case "setEmissivity":
                device.setEmissivity(
                        getValue(int.class, "emissivity", m, getEmissivity())
                    );
                break;

            case "getEmissivity":
                response = device.getEmissivity();
                break;

            case "setAmbientTemperatureCallbackPeriod":
                device.setAmbientTemperatureCallbackPeriod(
                        getValue(long.class, "period", m, getPeriod())
                    );
                break;

            case "getAmbientTemperatureCallbackPeriod":
                response = device.getAmbientTemperatureCallbackPeriod();
                break;

            case "setObjectTemperatureCallbackPeriod":
                device.setObjectTemperatureCallbackPeriod(
                        getValue(long.class, "period2", m, getPeriod2())
                    );
                break;

            case "getObjectTemperatureCallbackPeriod":
                response = device.getObjectTemperatureCallbackPeriod();
                break;

            case "setAmbientTemperatureCallbackThreshold":
                device.setAmbientTemperatureCallbackThreshold(
                        getValue(char.class, "option", m, getOption()),
                        getValue(short.class, "min", m, getMin()),
                        getValue(short.class, "max", m, getMax())
                    );
                break;

            case "getAmbientTemperatureCallbackThreshold":
                response = device.getAmbientTemperatureCallbackThreshold();
                break;

            case "setObjectTemperatureCallbackThreshold":
                device.setObjectTemperatureCallbackThreshold(
                        getValue(char.class, "option2", m, getOption2()),
                        getValue(short.class, "min2", m, getMin2()),
                        getValue(short.class, "max2", m, getMax2())
                    );
                break;

            case "getObjectTemperatureCallbackThreshold":
                response = device.getObjectTemperatureCallbackThreshold();
                break;

            case "setDebouncePeriod":
                device.setDebouncePeriod(
                        getValue(long.class, "debounce", m, getDebounce())
                    );
                break;

            case "getDebouncePeriod":
                response = device.getDebouncePeriod();
                break;

            case "getIdentity":
                response = device.getIdentity();
                break;


            default: throw new Exception("unknown function '"+function+"'");
            
        }
        
        return response;
    }
    
    
    /**
     * 
     * Sets the `emissivity <https://en.wikipedia.org/wiki/Emissivity>`__ that is
     * used to calculate the surface temperature as returned by 
     * :func:`GetObjectTemperature`. 
     * 
     * The emissivity is usually given as a value between 0.0 and 1.0. A list of
     * emissivities of different materials can be found 
     * `here <http://www.infrared-thermography.com/material.htm>`__.
     * 
     * The parameter of :func:`SetEmissivity` has to be given with a factor of
     * 65535 (16-bit). For example: An emissivity of 0.1 can be set with the
     * value 6553, an emissivity of 0.5 with the value 32767 and so on.
     * 
     * .. note::
     *  If you need a precise measurement for the object temperature, it is
     *  absolutely crucial that you also provide a precise emissivity.
     * 
     * The default emissivity is 1.0 (value of 65535) and the minimum emissivity the
     * sensor can handle is 0.1 (value of 6553).
     * 
     */
    public Integer getEmissivity(){
        return emissivity;
    }

    public void setEmissivity(Integer emissivity){
        this.emissivity = emissivity;
    }

    /**
     * 
     * Sets the period in ms with which the :func:`AmbientTemperature` callback is triggered
     * periodically. A value of 0 turns the callback off.
     * 
     * :func:`AmbientTemperature` is only triggered if the temperature has changed since the
     * last triggering.
     * 
     * The default value is 0.
     * 
     */
    public Long getPeriod(){
        return period;
    }

    public void setPeriod(Long period){
        this.period = period;
    }

    /**
     * 
     * Sets the period in ms with which the :func:`ObjectTemperature` callback is triggered
     * periodically. A value of 0 turns the callback off.
     * 
     * :func:`ObjectTemperature` is only triggered if the temperature has changed since the
     * last triggering.
     * 
     * The default value is 0.
     * 
     */
    public Long getPeriod2(){
        return period2;
    }

    public void setPeriod2(Long period2){
        this.period2 = period2;
    }

    /**
     * 
     * Sets the thresholds for the :func:`AmbientTemperatureReached` callback. 
     * 
     * The following options are possible:
     * 
     * .. csv-table::
     *  :header: "Option", "Description"
     *  :widths: 10, 100
     * 
     *  "'x'",    "Callback is turned off"
     *  "'o'",    "Callback is triggered when the ambient temperature is *outside* the min and max values"
     *  "'i'",    "Callback is triggered when the ambient temperature is *inside* the min and max values"
     *  "'<'",    "Callback is triggered when the ambient temperature is smaller than the min value (max is ignored)"
     *  "'>'",    "Callback is triggered when the ambient temperature is greater than the min value (max is ignored)"
     * 
     * The default value is ('x', 0, 0).
     * 
     */
    public Character getOption(){
        return option;
    }

    public void setOption(Character option){
        this.option = option;
    }

    /**
     * 
     * Sets the thresholds for the :func:`AmbientTemperatureReached` callback. 
     * 
     * The following options are possible:
     * 
     * .. csv-table::
     *  :header: "Option", "Description"
     *  :widths: 10, 100
     * 
     *  "'x'",    "Callback is turned off"
     *  "'o'",    "Callback is triggered when the ambient temperature is *outside* the min and max values"
     *  "'i'",    "Callback is triggered when the ambient temperature is *inside* the min and max values"
     *  "'<'",    "Callback is triggered when the ambient temperature is smaller than the min value (max is ignored)"
     *  "'>'",    "Callback is triggered when the ambient temperature is greater than the min value (max is ignored)"
     * 
     * The default value is ('x', 0, 0).
     * 
     */
    public Short getMin(){
        return min;
    }

    public void setMin(Short min){
        this.min = min;
    }

    /**
     * 
     * Sets the thresholds for the :func:`AmbientTemperatureReached` callback. 
     * 
     * The following options are possible:
     * 
     * .. csv-table::
     *  :header: "Option", "Description"
     *  :widths: 10, 100
     * 
     *  "'x'",    "Callback is turned off"
     *  "'o'",    "Callback is triggered when the ambient temperature is *outside* the min and max values"
     *  "'i'",    "Callback is triggered when the ambient temperature is *inside* the min and max values"
     *  "'<'",    "Callback is triggered when the ambient temperature is smaller than the min value (max is ignored)"
     *  "'>'",    "Callback is triggered when the ambient temperature is greater than the min value (max is ignored)"
     * 
     * The default value is ('x', 0, 0).
     * 
     */
    public Short getMax(){
        return max;
    }

    public void setMax(Short max){
        this.max = max;
    }

    /**
     * 
     * Sets the thresholds for the :func:`ObjectTemperatureReached` callback. 
     * 
     * The following options are possible:
     * 
     * .. csv-table::
     *  :header: "Option", "Description"
     *  :widths: 10, 100
     * 
     *  "'x'",    "Callback is turned off"
     *  "'o'",    "Callback is triggered when the object temperature is *outside* the min and max values"
     *  "'i'",    "Callback is triggered when the object temperature is *inside* the min and max values"
     *  "'<'",    "Callback is triggered when the object temperature is smaller than the min value (max is ignored)"
     *  "'>'",    "Callback is triggered when the object temperature is greater than the min value (max is ignored)"
     * 
     * The default value is ('x', 0, 0).
     * 
     */
    public Character getOption2(){
        return option2;
    }

    public void setOption2(Character option2){
        this.option2 = option2;
    }

    /**
     * 
     * Sets the thresholds for the :func:`ObjectTemperatureReached` callback. 
     * 
     * The following options are possible:
     * 
     * .. csv-table::
     *  :header: "Option", "Description"
     *  :widths: 10, 100
     * 
     *  "'x'",    "Callback is turned off"
     *  "'o'",    "Callback is triggered when the object temperature is *outside* the min and max values"
     *  "'i'",    "Callback is triggered when the object temperature is *inside* the min and max values"
     *  "'<'",    "Callback is triggered when the object temperature is smaller than the min value (max is ignored)"
     *  "'>'",    "Callback is triggered when the object temperature is greater than the min value (max is ignored)"
     * 
     * The default value is ('x', 0, 0).
     * 
     */
    public Short getMin2(){
        return min2;
    }

    public void setMin2(Short min2){
        this.min2 = min2;
    }

    /**
     * 
     * Sets the thresholds for the :func:`ObjectTemperatureReached` callback. 
     * 
     * The following options are possible:
     * 
     * .. csv-table::
     *  :header: "Option", "Description"
     *  :widths: 10, 100
     * 
     *  "'x'",    "Callback is turned off"
     *  "'o'",    "Callback is triggered when the object temperature is *outside* the min and max values"
     *  "'i'",    "Callback is triggered when the object temperature is *inside* the min and max values"
     *  "'<'",    "Callback is triggered when the object temperature is smaller than the min value (max is ignored)"
     *  "'>'",    "Callback is triggered when the object temperature is greater than the min value (max is ignored)"
     * 
     * The default value is ('x', 0, 0).
     * 
     */
    public Short getMax2(){
        return max2;
    }

    public void setMax2(Short max2){
        this.max2 = max2;
    }

    /**
     * 
     * Sets the period in ms with which the threshold callbacks
     * 
     * * :func:`AmbientTemperatureReached`,
     * * :func:`ObjectTemperatureReached`
     * 
     * are triggered, if the thresholds
     * 
     * * :func:`SetAmbientTemperatureCallbackThreshold`,
     * * :func:`SetObjectTemperatureCallbackThreshold`
     * 
     * keep being reached.
     * 
     * The default value is 100.
     * 
     */
    public Long getDebounce(){
        return debounce;
    }

    public void setDebounce(Long debounce){
        this.debounce = debounce;
    }



}
