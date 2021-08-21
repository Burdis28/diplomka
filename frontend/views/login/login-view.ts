import {LitElement, html, customElement} from 'lit-element';
import '@vaadin/vaadin-form-layout/src/vaadin-form-layout.js';
import '@vaadin/vaadin-text-field/src/vaadin-password-field.js';
import '@vaadin/vaadin-text-field/src/vaadin-text-field.js';
import '@vaadin/vaadin-button/src/vaadin-button.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js';

@customElement('login-view')
export class LoginView extends LitElement {
    createRenderRoot() {
        // Do not use a shadow root
        return this;
    }

    render() {
        return html`
<vaadin-vertical-layout style="width: 100%; height: 100%; align-items: center; justify-content: center;" id="vaadinVerticalLayout">
 <vaadin-horizontal-layout style="width: 370px; height: 250px; justify-content: center; align-items: flex-end; align-self: center;">
  <div id="imageDiv" style="width: 370px; height: 250px; align-self: center; margin-left: var(--lumo-space-s);"></div>
 </vaadin-horizontal-layout>
 <vaadin-form-layout id="vaadinFormLayout" style="align-self: center;">
  <vaadin-vertical-layout theme="spacing" id="vaadinVerticalLayout1">
   <h3 id="loginTitleH3" style="align-self: flex-start; margin-top: var(--lumo-space-l); margin-bottom: var(--lumo-space-xs);">Login</h3>
   <vaadin-text-field label="Username" placeholder="username" id="username" style="align-self: center; margin-top: var(--lumo-space-xs);" required invalid></vaadin-text-field>
   <vaadin-password-field label="Password" id="password" style="align-self: center;" required has-value invalid></vaadin-password-field>
   <vaadin-horizontal-layout theme="spacing">
    <vaadin-button id="loginButton" style="align-self: flex-start;">
      Log in 
    </vaadin-button>
    <vaadin-button id="registerButton">
     Forgot password?
    </vaadin-button>
   </vaadin-horizontal-layout>
  </vaadin-vertical-layout>
 </vaadin-form-layout>
</vaadin-vertical-layout>
`;
    }
}