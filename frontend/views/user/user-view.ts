import { LitElement, html, css, customElement } from 'lit-element';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js';
import '@vaadin/vaadin-form-layout/vaadin-form-item.js';
import '@vaadin/vaadin-text-field/vaadin-text-field.js';
import '@vaadin/vaadin-button/src/vaadin-button.js';
import '@polymer/iron-icon/iron-icon.js';

@customElement('user-view')
export class UserView extends LitElement {
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
<vaadin-vertical-layout theme="spacing" style="width: 100%; height: 100%; margin: var(--lumo-space-l); padding: var(--lumo-space-m); justify-content: flex-start; align-items: center;">
 <vaadin-form-item id="vaadinFormItem">
  <label slot="label" id="label">First name</label>
  <vaadin-text-field class="full-width" id="firstNameField" readonly></vaadin-text-field>
 </vaadin-form-item>
 <vaadin-form-item id="vaadinFormItem1">
  <label slot="label" id="label1">Last name</label>
  <vaadin-text-field class="full-width" readonly id="lastNameField"></vaadin-text-field>
 </vaadin-form-item>
 <vaadin-form-item id="vaadinFormItem1">
  <label slot="label" id="label1">Phone</label>
  <vaadin-text-field class="full-width" readonly id="phoneField"></vaadin-text-field>
 </vaadin-form-item>
 <vaadin-form-item id="vaadinFormItem1">
  <label slot="label" id="label1">Email </label>
  <vaadin-text-field class="full-width" readonly id="emailField"></vaadin-text-field>
 </vaadin-form-item>
 <vaadin-horizontal-layout theme="spacing">
  <vaadin-button id="editButton" theme="primary">
   <iron-icon icon="lumo:edit" slot="prefix"></iron-icon> Edit 
  </vaadin-button>
  <vaadin-button id="saveButton" theme="primary">
   Save 
  </vaadin-button>
  <vaadin-button id="cancelButton" theme="secondary">
   Cancel
  </vaadin-button>
 </vaadin-horizontal-layout>
</vaadin-vertical-layout>
`;
  }

  // Remove this method to render the contents of this view inside Shadow DOM
  createRenderRoot() {
    return this;
  }
}
