import { LitElement, html, css, customElement } from 'lit-element';
import '@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';

@customElement('water-sensor-view')
export class WaterSensorView extends LitElement {
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
<vaadin-vertical-layout style="width: 100%; height: 100%; align-items: center; justify-content: flex-start;" theme="spacing">
 <div id="wrapperDiv" style="align-self: center;">
  <vaadin-horizontal-layout theme="spacing" id="vaadinHorizontalLayout">
   <vaadin-horizontal-layout theme="spacing" id="firstLayout"></vaadin-horizontal-layout>
   <vaadin-vertical-layout style="width: 100%; height: 100%; align-items: center; justify-content: center;" theme="spacing-s" id="verticalLayoutElectric">
    <h3 id="electricAttributesTitle" style="align-self: flex-start;">Water attributes</h3>
    <vaadin-big-decimal-field label="Price per m3" id="pricePerM3Field" style="align-self: center; padding: var(--lumo-space-m);" tag="vaadin-big-decimal-field">
     <div id="pricePerM3FieldSuffix" slot="suffix">
       Kč 
     </div>
    </vaadin-big-decimal-field>
    <vaadin-big-decimal-field label="Impl per liter" id="implPerLiterField" style="align-self: center; flex-grow: 0; padding: var(--lumo-space-m);" tag="vaadin-big-decimal-field"></vaadin-big-decimal-field>
    <vaadin-big-decimal-field label="Count stop" style="align-self: center; padding: var(--lumo-space-m);" id="countStopField" tag="vaadin-big-decimal-field"></vaadin-big-decimal-field>
    <vaadin-big-decimal-field label="Count stop night" style="align-self: center; padding: var(--lumo-space-m);" id="countStopNightField" tag="vaadin-big-decimal-field"></vaadin-big-decimal-field>
    <vaadin-big-decimal-field label="Night hour start" style="align-self: center; padding: var(--lumo-space-m);" id="nightStartHourField" tag="vaadin-big-decimal-field"></vaadin-big-decimal-field>
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
