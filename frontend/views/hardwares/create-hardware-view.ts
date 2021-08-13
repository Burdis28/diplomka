import { LitElement, html, css, customElement } from 'lit-element';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js';
import '@vaadin/vaadin-button/src/vaadin-button.js';
import '@vaadin/vaadin-form-layout/src/vaadin-form-item.js';
import '@vaadin/vaadin-text-field/src/vaadin-text-field.js';

@customElement('create-hardware-view')
export class CreateHardwareView extends LitElement {
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
   <h3>Create new hardware</h3>
   <vaadin-form-item>
    <label slot="label">Name</label>
    <vaadin-text-field label="" placeholder="" id="nameField" maxlength="45"></vaadin-text-field>
   </vaadin-form-item>
   <vaadin-form-item>
    <label slot="label">Serial hardware code</label>
    <vaadin-text-field label="" placeholder="" id="serialHwCodeField" maxlength="45"></vaadin-text-field>
   </vaadin-form-item>
   <vaadin-horizontal-layout theme="spacing" style="align-self: center;">
    <vaadin-button theme="primary" id="createHwButton">
     Create
    </vaadin-button>
    <vaadin-button theme="secondary" id="cancelButton">
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
