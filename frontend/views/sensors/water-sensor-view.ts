import { LitElement, html, css, customElement } from 'lit-element';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';
import '@vaadin/vaadin-select/src/vaadin-select.js';
import '@vaadin/vaadin-list-box/src/vaadin-list-box.js';
import '@vaadin/vaadin-item/src/vaadin-item.js';
import '@vaadin/vaadin-text-field/src/vaadin-text-field.js';
import '@vaadin/vaadin-time-picker/src/vaadin-time-picker.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js';

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
    <vaadin-horizontal-layout theme="spacing">
     <vaadin-text-field label="Price per m3" id="pricePerM3Field" required clear-button-visible></vaadin-text-field>
     <vaadin-text-field label="Impl per liter" id="implPerLitField" required clear-button-visible></vaadin-text-field>
    </vaadin-horizontal-layout>
    <vaadin-horizontal-layout theme="spacing">
     <vaadin-select id="stateSelect" label="State" required>
      <template>
       <vaadin-list-box>
        <vaadin-item>
          Item one 
        </vaadin-item>
        <vaadin-item>
          Item two 
        </vaadin-item>
        <vaadin-item>
          Item three 
        </vaadin-item>
       </vaadin-list-box>
      </template>
     </vaadin-select>
     <vaadin-text-field label="Time between impl" id="timeBetweenImplField" required></vaadin-text-field>
    </vaadin-horizontal-layout>
    <vaadin-horizontal-layout theme="spacing">
     <vaadin-text-field label="State last modified by" id="stateModifiedBy" readonly helper-text="If not filled - state wasn't changed yet."></vaadin-text-field>
     <vaadin-text-field label="State modified date" id="stateModifiedDateField" readonly></vaadin-text-field>
    </vaadin-horizontal-layout>
    <vaadin-horizontal-layout theme="spacing">
     <vaadin-text-field label="Count stop" id="countStopField" required></vaadin-text-field>
     <vaadin-text-field label="Count stop at night" id="countStopNightField" required></vaadin-text-field>
    </vaadin-horizontal-layout>
    <vaadin-horizontal-layout theme="spacing">
     <vaadin-time-picker id="nightStartTimeField" max="06:00" label="Night start time" required min="21:00"></vaadin-time-picker>
     <vaadin-time-picker id="nightEndTimeField" max="21:00" label="Night end time" required min="06:00"></vaadin-time-picker>
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
