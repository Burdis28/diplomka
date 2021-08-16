import { customElement, html, LitElement } from 'lit-element';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';
import '@vaadin/vaadin-button';
import '@vaadin/vaadin-select/src/vaadin-select.js';
import '@vaadin/vaadin-form-layout';
import '@vaadin/vaadin-text-field';
import '@vaadin/vaadin-text-field/src/vaadin-number-field.js';
import '@vaadin/vaadin-form-layout/src/vaadin-form-layout.js';
import '@vaadin/vaadin-text-field/src/vaadin-text-field.js';

@customElement('createsensor-view')
export class CreatesensorView extends LitElement {
  createRenderRoot() {
    // Do not use a shadow root
    return this;
  }

  render() {
    return html`
<vaadin-vertical-layout theme="spacing" style="width: 100%; height: 100%; align-items: center;" id="overallLayout">
 <div id="div" class="wrapper" style="width: 100%; height: 100%; margin: var(--lumo-space-m);">
  <h3>Create new sensor </h3>
  <vaadin-form-layout style="width: 100%;" id="basicSensorAttributesForm">
   <vaadin-text-field label="Name of the sensor" id="nameField" required invalid></vaadin-text-field>
   <vaadin-text-field label="Daily limit" id="limitDayField" required helper-text="Valid input format: 123.000" invalid></vaadin-text-field>
   <vaadin-text-field label="Currency" id="currencyField" required invalid helper-text="Default currency is Kč"></vaadin-text-field>
   <vaadin-text-field label="Monthly limit" id="limitMonthField" required helper-text="Valid input format: 1234567.000" invalid></vaadin-text-field>
   <vaadin-select id="typeSelect" label="Type of sensor" placeholder="Type" required invalid></vaadin-select>
   <vaadin-text-field id="pinIdField" required label="Pin ID" prevent-invalid-input helper-text="Sensor PIN number" invalid></vaadin-text-field>
   <vaadin-select label="Attach to hardware" id="attachToHardware" placeholder="Hardware serial number" required invalid helper-text="Sensor has to be attached to a physical hardware unit"></vaadin-select>
  </vaadin-form-layout>
  <span id="electricAllAttributesLayout"><h3>Electric attributes - price</h3>
   <vaadin-form-layout id="electricLayout" style="margin-top: var(--lumo-space-xl);">
    <vaadin-text-field label="Price per KW - low" id="pricePerKwLowField" required helper-text="Valid input format: 1234.00" invalid></vaadin-text-field>
    <vaadin-text-field label="Price per KW - high" id="pricePerKwHighField" required helper-text="Valid input format: 1234.00" invalid></vaadin-text-field>
    <vaadin-text-field label="Fixed price" id="fixedPriceField" required helper-text="Valid input format: 1234.00" invalid></vaadin-text-field>
    <vaadin-text-field label="Service price" id="servicePriceField" required helper-text="Valid input format: 1234.00" invalid></vaadin-text-field>
   </vaadin-form-layout><h3>Electric attributes - impulse</h3>
   <vaadin-form-layout>
    <vaadin-text-field label="Impl. per KW" id="implPerKwField" required helper-text="Valid input format: non-decimal number" invalid></vaadin-text-field>
   </vaadin-form-layout></span>
  <span id="waterAllAttributesLayout"><h3 style="margin-top: var(--lumo-space-xl);">Water attributes - price</h3>
   <vaadin-form-layout id="waterLayout">
    <vaadin-text-field id="pricePerM3Field" required label="Price per m3" helper-text="Valid input format: 1234.00" invalid></vaadin-text-field>
    <vaadin-number-field id="implPerLitField" required label="Impl. per liter" prevent-invalid-input invalid helper-text="Valid input: non decimal number"></vaadin-number-field>
   </vaadin-form-layout><h3>Water attributes - others</h3>
   <vaadin-form-layout>
    <vaadin-text-field id="nightStartField" required label="Time of start at night" helper-text="Valid input format: HH:MM"></vaadin-text-field>
    <vaadin-text-field id="nightEndField" required label="Time of end at night" helper-text="Valid input format: HH:MM"></vaadin-text-field>
   </vaadin-form-layout></span>
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
