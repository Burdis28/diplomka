import { LitElement, html, css, customElement } from 'lit-element';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';
import '@vaadin/vaadin-form-layout/vaadin-form-item.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js';
import '@vaadin/vaadin-button/src/vaadin-button.js';
import '@polymer/iron-icon/iron-icon.js';
import '@vaadin/vaadin-text-field/src/vaadin-password-field.js';
import '@vaadin/vaadin-text-field/vaadin-text-field.js';
import '@vaadin/vaadin-text-field/src/vaadin-email-field.js';

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
 <div id="formDiv">
  <vaadin-vertical-layout theme="spacing">
   <h3>My profile</h3>
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
    <vaadin-email-field id="emailField" readonly></vaadin-email-field>
   </vaadin-form-item>
   <vaadin-form-item id="passwordChangeForm1">
    <label slot="label" id="label1">New password</label>
    <vaadin-password-field placeholder="Enter password" id="passwordField1" has-value></vaadin-password-field>
   </vaadin-form-item>
   <vaadin-form-item id="passwordChangeForm2">
    <label slot="label" id="label1">New password again</label>
    <vaadin-password-field placeholder="Enter password" has-value id="passwordField2"></vaadin-password-field>
   </vaadin-form-item>
   <vaadin-horizontal-layout theme="spacing" style="align-self: center; padding: var(--lumo-space-l); justify-content: center;">
    <vaadin-button id="editButton" theme="primary">
     <iron-icon icon="lumo:edit" slot="prefix"></iron-icon> Edit 
    </vaadin-button>
    <vaadin-button id="changePasswordButton" theme="primary">
      Change password 
    </vaadin-button>
    <vaadin-button id="saveButton" theme="primary">
      Save 
    </vaadin-button>
    <vaadin-button id="cancelButton" theme="secondary">
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
