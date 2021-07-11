import { LitElement, html, css, customElement } from 'lit-element';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';
import '@vaadin/vaadin-charts/src/vaadin-chart.js';
import '@vaadin/vaadin-form-layout/vaadin-form-item.js';
import '@vaadin/vaadin-text-field/vaadin-text-field.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js';
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
 <div id="formDiv">
  <vaadin-vertical-layout theme="spacing">
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
 </div>
 <vaadin-chart type="pie" title="Browser market shares in January, 2018" tooltip="" id="vaadinChart" chart3d>
  <vaadin-chart-series title="Brands" values="[[&quot;Chrome&quot;,61.41],[&quot;Internet Explorer&quot;,11.84],[&quot;Firefox&quot;,10.85],[&quot;Edge&quot;,4.67],[&quot;Safari&quot;,4.18],[&quot;Sogou Explorer&quot;,1.64],[&quot;Opera&quot;,1.6],[&quot;QQ&quot;,1.2],[&quot;Other&quot;,2.61]]"></vaadin-chart-series>
 </vaadin-chart>
</vaadin-vertical-layout>
`;
  }

  // Remove this method to render the contents of this view inside Shadow DOM
  createRenderRoot() {
    return this;
  }
}
