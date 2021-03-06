import { LitElement, html, css, customElement } from 'lit-element';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';
import '@vaadin/vaadin-button/src/vaadin-button.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js';
import '@vaadin/vaadin-text-field/src/vaadin-text-field.js';

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
    <vaadin-horizontal-layout theme="spacing" style="align-self: stretch;">
     <vaadin-text-field label="Price per m3" id="pricePerM3Field" required clear-button-visible invalid></vaadin-text-field>
    </vaadin-horizontal-layout>
    <vaadin-horizontal-layout theme="spacing" style="width: 100%;">
     <vaadin-text-field label="Current state" id="stateTextField" required></vaadin-text-field>
     <vaadin-button id="openCloseValveButton" style="align-self: center; margin-top: 2.2em; margin-left: var(--lumo-space-xl);" theme="primary">
       Open valve 
     </vaadin-button>
    </vaadin-horizontal-layout>
    <vaadin-horizontal-layout theme="spacing">
     <vaadin-text-field label="State last modified by" id="stateModifiedBy" readonly helper-text="If not filled - state wasn't changed yet."></vaadin-text-field>
     <vaadin-text-field label="State modified date" id="stateModifiedDateField" readonly></vaadin-text-field>
    </vaadin-horizontal-layout>
    <vaadin-horizontal-layout theme="spacing">
     <vaadin-text-field label="Time[min] to stop" id="countStopField" required invalid></vaadin-text-field>
     <vaadin-text-field label="Time[min] to stop at night" id="countStopNightField" required invalid></vaadin-text-field>
    </vaadin-horizontal-layout>
    <vaadin-horizontal-layout theme="spacing">
     <vaadin-text-field id="startNightField" required label="Time of start at night" helper-text="Valid input format: HH:MM"></vaadin-text-field>
     <vaadin-text-field id="endNightField" required label="Time of end at night" helper-text="Valid input format: HH:MM"></vaadin-text-field>
    </vaadin-horizontal-layout>
    <h3 id="configurationTitle" style="align-self: flex-start;">Water configuration</h3>
    <vaadin-horizontal-layout id="configurationLayout" style="align-self: stretch;" theme="spacing">
     <vaadin-text-field label="Impulse per liter" id="implPerLitField" required clear-button-visible invalid></vaadin-text-field>
    </vaadin-horizontal-layout>
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
