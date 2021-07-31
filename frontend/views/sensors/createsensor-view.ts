import { customElement, html, LitElement } from 'lit-element';
import '@vaadin/vaadin-form-layout';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';
import '@vaadin/vaadin-form-layout/src/vaadin-form-layout.js';
import '@vaadin/vaadin-text-field';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';
import '@vaadin/vaadin-button';
import '@vaadin/vaadin-checkbox/src/vaadin-checkbox.js';
import '@vaadin/vaadin-select/src/vaadin-select.js';
import '@vaadin/vaadin-text-field/src/vaadin-number-field.js';
import '@vaadin/vaadin-text-field/src/vaadin-text-field.js';
import '@vaadin/flow-frontend/vaadin-big-decimal-field.js';

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
   <vaadin-text-field label="Daily limit" id="limitDayField" required helper-text="Input format: 123.000" invalid></vaadin-text-field>
   <vaadin-text-field label="Currency" id="currencyField" required invalid helper-text="Default currency is Kč"></vaadin-text-field>
   <vaadin-text-field label="Monthly limit" id="limitMonthField" required prevent-invalid-input helper-text="Input format: 1234567.000" invalid></vaadin-text-field>
   <vaadin-select id="typeSelect" label="Type of sensor" placeholder="Type" required invalid></vaadin-select>
   <vaadin-text-field id="pinIdField" required label="Pin ID" prevent-invalid-input helper-text="Sensor PIN number" invalid></vaadin-text-field>
   <vaadin-select label="Attach to hardware" id="attachToHardware" placeholder="Hardware serial number" required invalid helper-text="Sensor has to be attached to a physical hardware unit"></vaadin-select>
  </vaadin-form-layout>
  <vaadin-form-layout id="electricLayout" style="margin-top: var(--lumo-space-xl);">
   <h3>Electric attributes</h3>
   <vaadin-checkbox id="highRateCheckBox">
     High rate 
   </vaadin-checkbox>
   <vaadin-text-field label="Price per KW - high" id="pricePerKwHighField" required prevent-invalid-input helper-text="Input format: 1234.00" invalid></vaadin-text-field>
   <vaadin-text-field label="Price per KW - low" id="pricePerKwLowField" required prevent-invalid-input helper-text="Input format: 1234.00" invalid></vaadin-text-field>
   <vaadin-text-field label="Fixed price" id="fixedPriceField" required prevent-invalid-input helper-text="Input format: 1234.00" invalid></vaadin-text-field>
   <vaadin-text-field label="Service price" id="servicePriceField" required prevent-invalid-input helper-text="Input format: 1234.00" invalid></vaadin-text-field>
   <vaadin-text-field label="Impl. per KW" id="implPerKwField" required prevent-invalid-input helper-text="Input format: non-decimal number" invalid></vaadin-text-field>
  </vaadin-form-layout>
  <vaadin-form-layout id="waterLayout" style="margin-top: var(--lumo-space-xl);">
   <h3>Water attributes</h3>
   <vaadin-big-decimal-field id="pricePerM3Field" required label="Price per m3" prevent-invalid-input invalid></vaadin-big-decimal-field>
   <vaadin-select id="waterStateSelect" label="State" required invalid></vaadin-select>
   <vaadin-number-field id="implPerLitField" required label="Impl. per liter" prevent-invalid-input invalid></vaadin-number-field>
   <vaadin-number-field id="nightStartStepField" has-controls required min="21" value="21.00" max="24" label="Night start time" prevent-invalid-input step="0.01"></vaadin-number-field>
   <vaadin-number-field id="nightEndStepField" has-controls required min="0" value="00.00" max="6" label="Night end time" prevent-invalid-input step="0.01"></vaadin-number-field>
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
