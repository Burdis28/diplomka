import '@vaadin/vaadin-button';
import '@vaadin/vaadin-combo-box';
import '@vaadin/vaadin-custom-field';
import '@vaadin/vaadin-date-picker';
import '@vaadin/vaadin-form-layout';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';
import '@vaadin/vaadin-text-field';
import '@vaadin/vaadin-text-field/vaadin-email-field';
import { customElement, html, LitElement } from 'lit-element';

@customElement('createsensor-view')
export class CreatesensorView extends LitElement {
  createRenderRoot() {
    // Do not use a shadow root
    return this;
  }

  render() {
    return html`
<h3>Personal information</h3>
<vaadin-form-layout style="width: 100%;">
 <vaadin-text-field label="First name" id="firstName"></vaadin-text-field>
 <vaadin-text-field label="Last name" id="lastName"></vaadin-text-field>
 <vaadin-date-picker id="birthday" label="Birthday"></vaadin-date-picker>
 <vaadin-custom-field id="phoneNumber" label="Phone number" value="	">
  <vaadin-horizontal-layout theme="spacing">
   <vaadin-combo-box id="pnCountryCode" style="width: 120px;" pattern="\\+\\d*" placeholder="Country" prevent-invalid-input></vaadin-combo-box>
   <vaadin-text-field id="pnNumber" style="flex-grow: 1;" pattern="\\d*" prevent-invalid-input></vaadin-text-field>
  </vaadin-horizontal-layout>
 </vaadin-custom-field>
 <vaadin-email-field id="email" label="Email address"></vaadin-email-field>
 <vaadin-text-field id="occupation" label="Occupation"></vaadin-text-field>
</vaadin-form-layout>
<vaadin-horizontal-layout style="margin-top: var(--lumo-space-m); margin-bottom: var(--lumo-space-l);" theme="spacing">
 <vaadin-button theme="primary" id="save">
   Save 
 </vaadin-button>
 <vaadin-button id="cancel">
   Cancel 
 </vaadin-button>
</vaadin-horizontal-layout>
`;
  }
}
