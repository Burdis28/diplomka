import { LitElement, html, css, customElement } from 'lit-element';
import '@vaadin/vaadin-button/src/vaadin-button.js';
import '@polymer/iron-icon/iron-icon.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js';

@customElement('electric-sensor-view')
export class ElectricSensorView extends LitElement {
  static get styles() {
    return css`
      :host {
          display: block;
          height: 100%;
      }
      `;
  }

  render() {
    return html`
<vaadin-vertical-layout theme="spacing" style="width: 100%; height: 100%; justify-content: flex-start; align-items: center;">
 <div style="align-self: center;" id="wrapperDiv">
  <vaadin-horizontal-layout theme="spacing" id="vaadinHorizontalLayout">
   <vaadin-horizontal-layout theme="spacing" id="firstLayout"></vaadin-horizontal-layout>
   <vaadin-vertical-layout style="width: 100%; height: 100%; align-items: center; justify-content: center; flex-grow: 1; min-width: 300px;" theme="spacing-s" id="verticalLayoutElectric">
    <h3 id="electricAttributesTitle" style="align-self: flex-start;">Electric attributes</h3>
    <vaadin-big-decimal-field label="Price per KW - low" id="pricePerKwLowField" style="align-self: stretch; padding: var(--lumo-space-m); width: 90%;" tag="vaadin-big-decimal-field">
     <div id="pricePerKwLowSuffix" slot="suffix">
       Kč 
     </div>
    </vaadin-big-decimal-field>
    <vaadin-big-decimal-field label="Price per KW - high" id="pricePerKwHighField" style="align-self: stretch; flex-grow: 0; padding: var(--lumo-space-m); width: 90%;" tag="vaadin-big-decimal-field">
     <div id="pricePerKwHighSuffix" slot="suffix">
       Kč 
     </div>
    </vaadin-big-decimal-field>
    <vaadin-big-decimal-field label="Price service" style="align-self: stretch; padding: var(--lumo-space-m); width: 90%;" id="priceServiceField" tag="vaadin-big-decimal-field">
     <div slot="suffix" id="priceServiceSuffix">
       Kč 
     </div>
    </vaadin-big-decimal-field>
    <vaadin-big-decimal-field label="Price fixed" style="align-self: stretch; padding: var(--lumo-space-m); width: 90%;" id="priceFixedField" tag="vaadin-big-decimal-field">
     <div slot="suffix" id="priceFixedSuffix">
       Kč 
     </div>
    </vaadin-big-decimal-field>
    <h3 id="configurationTitle" style="align-self: flex-start;">Electric configuration</h3>
    <vaadin-big-decimal-field label="Impulse per KW" style="align-self: stretch; padding: var(--lumo-space-m); width: 90%;" id="implPerKWField" tag="vaadin-big-decimal-field">
     <div id="implPerKwSuffix" slot="suffix">
       Kč 
     </div>
    </vaadin-big-decimal-field>
   </vaadin-vertical-layout>
  </vaadin-horizontal-layout>
  <vaadin-horizontal-layout theme="spacing" style="align-self: center; padding: var(--lumo-space-l); justify-content: center;">
   <vaadin-button id="editButton" theme="primary">
    <iron-icon icon="lumo:edit" slot="prefix"></iron-icon> Edit 
   </vaadin-button>
   <vaadin-button id="returnButton" theme="secondary">
     Return 
   </vaadin-button>
   <vaadin-button id="saveButton" theme="primary">
     Save 
   </vaadin-button>
   <vaadin-button id="cancelButton">
     Cancel 
   </vaadin-button>
  </vaadin-horizontal-layout>
 </div>
</vaadin-vertical-layout>
`;
  }

  // Remove this method to render the contents of this view inside Shadow DOM
  createRenderRoot() {
    return this;
  }
}
