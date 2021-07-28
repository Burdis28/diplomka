import { customElement, html, LitElement } from 'lit-element';
import '@vaadin/vaadin-form-layout';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';
import '@vaadin/vaadin-form-layout/src/vaadin-form-layout.js';
import '@vaadin/vaadin-text-field';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';
import '@vaadin/vaadin-button';
import '@vaadin/vaadin-checkbox/src/vaadin-checkbox.js';
import '@vaadin/flow-frontend/vaadin-big-decimal-field.js';
import '@vaadin/vaadin-time-picker/src/vaadin-time-picker.js';
import '@vaadin/vaadin-select/src/vaadin-select.js';
import '@vaadin/vaadin-text-field/src/vaadin-number-field.js';
import '@vaadin/vaadin-text-field/src/vaadin-text-field.js';

@customElement('createsensor-view')
export class CreatesensorView extends LitElement {
  createRenderRoot() {
    // Do not use a shadow root
    return this;
  }

  render() {
    return html`
<vaadin-vertical-layout theme="spacing" style="width: 100%; height: 100%; align-items: center;">
 <div id="div" class="wrapper" style="width: 100%; height: 100%; margin: var(--lumo-space-m);">
  <h3>Create new sensor </h3>
  <vaadin-form-layout style="width: 100%;" id="basicSensorAttributesForm">
   <vaadin-text-field label="Name of the sensor" id="nameField" required invalid></vaadin-text-field>
   <vaadin-big-decimal-field id="limitDayField" required label="Daily limit" invalid></vaadin-big-decimal-field>
   <vaadin-text-field label="Currency" id="currencyField" required invalid helper-text="Default currency is Kč"></vaadin-text-field>
   <vaadin-big-decimal-field id="limitMonthField" required label="Monthly limit" invalid></vaadin-big-decimal-field>
   <vaadin-select id="typeSelect" label="Type of sensor" placeholder="Type" required invalid></vaadin-select>
   <vaadin-text-field id="pinIdField" required label="Pin ID" prevent-invalid-input helper-text="Sensor PIN"></vaadin-text-field>
   <vaadin-select label="Attach to hardware" id="attachToHardware" placeholder="Hardware serial number" required invalid helper-text="Sensor has to be attached to a physical hardware unit"></vaadin-select>
  </vaadin-form-layout>
  <vaadin-form-layout id="electricLayout" style="margin-top: var(--lumo-space-xl);">
   <h3>Electric attributes</h3>
   <vaadin-checkbox id="highRateCheckBox">
     High rate 
   </vaadin-checkbox>
   <vaadin-big-decimal-field id="pricePerKwHighField" label="Price per KW - high" required></vaadin-big-decimal-field>
   <vaadin-big-decimal-field id="pricePerKwLowField" label="Price per KW - low" required></vaadin-big-decimal-field>
   <vaadin-big-decimal-field id="fixedPriceField" label="Fixed price" required></vaadin-big-decimal-field>
   <vaadin-big-decimal-field id="servicePriceField" label="Service price" required></vaadin-big-decimal-field>
   <vaadin-big-decimal-field id="implPerKwField" label="Impl. per KW" required></vaadin-big-decimal-field>
  </vaadin-form-layout>
  <vaadin-form-layout id="waterLayout" style="margin-top: var(--lumo-space-xl);">
   <h3>Water attributes</h3>
   <vaadin-big-decimal-field id="pricePerM3Field" required label="Price per m3"></vaadin-big-decimal-field>
   <vaadin-select id="waterStateSelect" label="State" required></vaadin-select>
   <vaadin-number-field id="implPerLitField" required label="Impl. per liter"></vaadin-number-field>
   <vaadin-time-picker id="nightStartTimePicker" max="06:00" label="Night start time" required min="21:00" clear-button-visible prevent-invalid-input error-message="Wrong input" helper-text="Pick a time when the sensor is gonna start scaning at night."></vaadin-time-picker>
   <vaadin-time-picker label="Night end time" id="nightEndTimePicker" max="06:00" required min="21:00" clear-button-visible prevent-invalid-input error-message="Wrong input" helper-text="Pick a time when the sensor is gonna end scaning at night."></vaadin-time-picker>
  </vaadin-form-layout>
  <vaadin-horizontal-layout style="margin-top: var(--lumo-space-m); margin-bottom: var(--lumo-space-l); justify-content: center; padding-bottom: var(--lumo-space-l); padding-top: var(--lumo-space-l);" theme="spacing">
   <vaadin-button theme="primary" id="save">
     Create 
   </vaadin-button>
   <vaadin-button id="cancel">
     Cancel 
   </vaadin-button>
  </vaadin-horizontal-layout>
 </div>
</vaadin-vertical-layout>
`;
  }
}
