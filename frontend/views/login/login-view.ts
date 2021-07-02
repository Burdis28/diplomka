import {LitElement, html, css, customElement} from 'lit-element';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';
import '@vaadin/vaadin-form-layout/src/vaadin-form-layout.js';
import '@vaadin/vaadin-text-field/src/vaadin-text-field.js';
import '@vaadin/vaadin-text-field/src/vaadin-password-field.js';
import '@vaadin/vaadin-button/src/vaadin-button.js';

@customElement('login-view')
export class LoginView extends LitElement {
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
<vaadin-vertical-layout style="width: 100%; height: 100%; align-items: center; justify-content: center;" id="vaadinVerticalLayout">
 <h1 id="Title" style="align-self: center;">Diplomka - Správa chytrého domu</h1>
 <vaadin-form-layout id="vaadinFormLayout" style="align-self: center;">
  <vaadin-vertical-layout theme="spacing" id="vaadinVerticalLayout1">
   <vaadin-text-field label="Username" placeholder="username" id="username" style="align-self: center;" required autoselect autofocus clear-button-visible invalid></vaadin-text-field>
   <vaadin-password-field label="Password" id="password" style="align-self: center;" required has-value invalid></vaadin-password-field>
   <vaadin-button id="loginButton" style="align-self: flex-start;">
     Log in 
   </vaadin-button>
  </vaadin-vertical-layout>
 </vaadin-form-layout>
</vaadin-vertical-layout>
`;
    }

    // Remove this method to render the contents of this view inside Shadow DOM
    createRenderRoot() {
        return this;
    }
}