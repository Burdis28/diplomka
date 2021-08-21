import { LitElement, html, css, customElement } from 'lit-element';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';
import '@vaadin/vaadin-text-field/src/vaadin-text-field.js';
import '@vaadin/vaadin-form-layout/src/vaadin-form-item.js';
import '@vaadin/vaadin-select/src/vaadin-select.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js';
import '@vaadin/vaadin-button/src/vaadin-button.js';
import '@polymer/iron-icon/iron-icon.js';

@customElement('hardware-detail')
export class HardwareDetail extends LitElement {
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
<vaadin-vertical-layout style="width: 100%; height: 100%; margin: var(--lumo-space-l); padding: var(--lumo-space-m); justify-content: flex-start; align-items: center;" theme="spacing">
 <div id="formDiv">
  <vaadin-vertical-layout theme="spacing">
   <h3>Hardware detail</h3>
   <vaadin-form-item>
    <label slot="label">Name</label>
    <vaadin-text-field id="nameField" readonly maxlength="45"></vaadin-text-field>
   </vaadin-form-item>
   <vaadin-form-item>
    <label slot="label">Serial code </label>
    <vaadin-text-field id="serialCodeField" readonly maxlength="45"></vaadin-text-field>
   </vaadin-form-item>
   <vaadin-form-item>
    <label slot="label">Online status</label>
    <vaadin-horizontal-layout style="justify-content: center; align-items: center;">
     <span id="onlineStatusDiv"></span>
    </vaadin-horizontal-layout>
   </vaadin-form-item>
   <vaadin-form-item>
    <label slot="label" style="align-self: flex-start;">Signal status</label>
    <vaadin-horizontal-layout style="justify-content: center; align-items: center;">
     <span id="signalImageDiv"></span>
    </vaadin-horizontal-layout>
   </vaadin-form-item>
   <vaadin-form-item id="vaadinFormItem">
    <label slot="label">Version</label>
    <vaadin-text-field id="versionField" readonly></vaadin-text-field>
   </vaadin-form-item>
   <vaadin-form-item id="attachedSensorsFormItem">
    <label slot="label">Attached sensors </label>
    <vaadin-select id="attachedSensorsSelect"></vaadin-select>
   </vaadin-form-item>
   <vaadin-form-item id="ownersFormItem">
    <label slot="label">Owners</label>
    <vaadin-select id="ownersSelect"></vaadin-select>
   </vaadin-form-item>
   <vaadin-horizontal-layout theme="spacing" style="align-self: center;">
    <vaadin-button id="editButton" theme="primary">
     <iron-icon icon="lumo:edit" slot="prefix"></iron-icon> Edit 
    </vaadin-button>
    <vaadin-button theme="primary" id="saveButton">
      Save 
    </vaadin-button>
    <vaadin-button id="cancelButton">
      Cancel 
    </vaadin-button>
   </vaadin-horizontal-layout>
  </vaadin-vertical-layout>
 </div>
</vaadin-vertical-layout>
`;
  }

  // Remove this method to render the contents of this view inside Shadow DOM
  createRenderRoot() {
    return this;
  }
}
