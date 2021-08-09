import { customElement, html, LitElement } from 'lit-element';
import '@vaadin/vaadin-form-layout';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';
import '@vaadin/vaadin-form-layout/src/vaadin-form-layout.js';
import '@vaadin/vaadin-text-field';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';
import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field/src/vaadin-text-field.js';
import '@vaadin/vaadin-text-field/src/vaadin-number-field.js';
import '@vaadin/flow-frontend/vaadin-big-decimal-field.js';
import '@vaadin/vaadin-checkbox/src/vaadin-checkbox.js';
import '@vaadin/vaadin-select/src/vaadin-select.js';

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
   <vaadin-text-field label="Name of the sensor" id="nameField"></vaadin-text-field>
   <vaadin-number-field id="limitDayField" label="Daily limit"></vaadin-number-field>
   <vaadin-text-field label="Currency" id="currencyField"></vaadin-text-field>
   <vaadin-number-field id="limitMonthField" label="Monthly limit"></vaadin-number-field>
   <vaadin-select id="typeSelect" label="Type of sensor" placeholder="Type"></vaadin-select>
   <vaadin-select label="Attach to hardware" id="attachToHardware" placeholder="Hardware serial number"></vaadin-select>
  </vaadin-form-layout>
  <vaadin-form-layout id="electricLayout" style="margin-top: var(--lumo-space-xl);">
   <h3>Electric attributes</h3>
   <vaadin-checkbox id="highRateCheckBox">
     High rate 
   </vaadin-checkbox>
   <vaadin-big-decimal-field id="pricePerKwHighField" label="Price per KW - high"></vaadin-big-decimal-field>
   <vaadin-big-decimal-field id="pricePerKwLowField" label="Price per KW - low"></vaadin-big-decimal-field>
   <vaadin-big-decimal-field id="fixedPriceField" label="Fixed price"></vaadin-big-decimal-field>
   <vaadin-big-decimal-field id="servicePriceField" label="Service price"></vaadin-big-decimal-field>
   <vaadin-big-decimal-field id="implPerKwField" label="Impl. per KW"></vaadin-big-decimal-field>
  </vaadin-form-layout>
  <vaadin-horizontal-layout style="margin-top: var(--lumo-space-m); margin-bottom: var(--lumo-space-l); justify-content: center;" theme="spacing">
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
