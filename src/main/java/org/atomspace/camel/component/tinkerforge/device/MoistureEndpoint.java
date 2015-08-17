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

import com.tinkerforge.BrickletMoisture;

/**
 * Measures soil moisture
 */
public class MoistureEndpoint extends TinkerforgeEndpoint<MoistureConsumer, MoistureProducer> {

    private static final Logger LOG = LoggerFactory.getLogger(MoistureEndpoint.class);
    
    private Long period;
    private Character option;
    private Integer min;
    private Integer max;
    private Long debounce;
    private Short average;

        
    public MoistureEndpoint(String uri, TinkerforgeComponent tinkerforgeComponent) {
        super(uri, tinkerforgeComponent);
    }

    @Override
    public Producer createProducer() throws Exception {
        LOG.trace("createProducer()");
        if(producer==null){
            producer = new MoistureProducer(this);
        }
        return producer;
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        LOG.trace("createConsumer(Processor processor='"+processor+"')");
        if(consumer==null){
            consumer = new MoistureConsumer(this, processor);
        }
        return consumer;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
    
    public void init(BrickletMoisture device) throws Exception {
        if(getInit()==null) return;
        
        String[] initFunctions = getInit().split(",");
        for (String function : initFunctions) {
            callFunction(device, function, null, this);
        }
    }
    
    public Object callFunction(BrickletMoisture device, String function, Message m, Endpoint e) throws Exception{
        Object response = null;
        switch (function) {
                
            case "getMoistureValue":
                response = device.getMoistureValue();
                break;

            case "setMoistureCallbackPeriod":
                device.setMoistureCallbackPeriod(
                        getValue(long.class, "period", m, getPeriod())
                    );
                break;

            case "getMoistureCallbackPeriod":
                response = device.getMoistureCallbackPeriod();
                break;

            case "setMoistureCallbackThreshold":
                device.setMoistureCallbackThreshold(
                        getValue(char.class, "option", m, getOption()),
                        getValue(int.class, "min", m, getMin()),
                        getValue(int.class, "max", m, getMax())
                    );
                break;

            case "getMoistureCallbackThreshold":
                response = device.getMoistureCallbackThreshold();
                break;

            case "setDebouncePeriod":
                device.setDebouncePeriod(
                        getValue(long.class, "debounce", m, getDebounce())
                    );
                break;

            case "getDebouncePeriod":
                response = device.getDebouncePeriod();
                break;

            case "setMovingAverage":
                device.setMovingAverage(
                        getValue(short.class, "average", m, getAverage())
                    );
                break;

            case "getMovingAverage":
                response = device.getMovingAverage();
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
     * Sets the period in ms with which the :func:`Moisture` callback is triggered
     * periodically. A value of 0 turns the callback off.
     * 
     * :func:`Moisture` is only triggered if the moisture value has changed since the
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
     * Sets the thresholds for the :func:`MoistureReached` callback. 
     * 
     * The following options are possible:
     * 
     * .. csv-table::
     *  :header: "Option", "Description"
     *  :widths: 10, 100
     * 
     *  "'x'",    "Callback is turned off"
     *  "'o'",    "Callback is triggered when the moisture value is *outside* the min and max values"
     *  "'i'",    "Callback is triggered when the moisture value is *inside* the min and max values"
     *  "'<'",    "Callback is triggered when the moisture value is smaller than the min value (max is ignored)"
     *  "'>'",    "Callback is triggered when the moisture value is greater than the min value (max is ignored)"
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
     * Sets the thresholds for the :func:`MoistureReached` callback. 
     * 
     * The following options are possible:
     * 
     * .. csv-table::
     *  :header: "Option", "Description"
     *  :widths: 10, 100
     * 
     *  "'x'",    "Callback is turned off"
     *  "'o'",    "Callback is triggered when the moisture value is *outside* the min and max values"
     *  "'i'",    "Callback is triggered when the moisture value is *inside* the min and max values"
     *  "'<'",    "Callback is triggered when the moisture value is smaller than the min value (max is ignored)"
     *  "'>'",    "Callback is triggered when the moisture value is greater than the min value (max is ignored)"
     * 
     * The default value is ('x', 0, 0).
     * 
     */
    public Integer getMin(){
        return min;
    }

    public void setMin(Integer min){
        this.min = min;
    }

    /**
     * 
     * Sets the thresholds for the :func:`MoistureReached` callback. 
     * 
     * The following options are possible:
     * 
     * .. csv-table::
     *  :header: "Option", "Description"
     *  :widths: 10, 100
     * 
     *  "'x'",    "Callback is turned off"
     *  "'o'",    "Callback is triggered when the moisture value is *outside* the min and max values"
     *  "'i'",    "Callback is triggered when the moisture value is *inside* the min and max values"
     *  "'<'",    "Callback is triggered when the moisture value is smaller than the min value (max is ignored)"
     *  "'>'",    "Callback is triggered when the moisture value is greater than the min value (max is ignored)"
     * 
     * The default value is ('x', 0, 0).
     * 
     */
    public Integer getMax(){
        return max;
    }

    public void setMax(Integer max){
        this.max = max;
    }

    /**
     * 
     * Sets the period in ms with which the threshold callback
     * 
     * * :func:`MoistureReached`
     * 
     * is triggered, if the threshold
     * 
     * * :func:`SetMoistureCallbackThreshold`
     * 
     * keeps being reached.
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

    /**
     * 
     * Sets the length of a `moving averaging <https://en.wikipedia.org/wiki/Moving_average>`__
     * for the moisture value.
     * 
     * Setting the length to 0 will turn the averaging completely off. With less
     * averaging, there is more noise on the data.
     * 
     * The range for the averaging is 0-100.
     * 
     * The default value is 100.
     * 
     */
    public Short getAverage(){
        return average;
    }

    public void setAverage(Short average){
        this.average = average;
    }



}
