import { LitElement, html, css, customElement } from 'lit-element';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';
import '@vaadin/vaadin-grid/src/vaadin-grid.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js';
import '@vaadin/vaadin-button/src/vaadin-button.js';

@customElement('user-management-view')
export class UserManagementView extends LitElement {
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
<vaadin-vertical-layout style="width: 100%; height: 100%;" theme="spacing">
 <div class="overallDiv" style="align-self: center; flex-grow: 1; height: 95%; width: 95%; flex-shrink: 1;">
  <vaadin-vertical-layout id="controlPanelLayout">
   <div style="align-self: center; flex-shrink: 1; flex-grow: 0;">
    <vaadin-horizontal-layout style="align-items: center; margin-top: -2em;">
     <h2 style="align-self: center;">Users management</h2>
    </vaadin-horizontal-layout>
   </div>
   <vaadin-button theme="primary" id="createUserBtn" style="align-self: center;">
     Create new user 
   </vaadin-button>
  </vaadin-vertical-layout>
  <vaadin-vertical-layout theme="spacing" id="gridLayout" style="margin: var(--lumo-space-m); margin-top: var(--lumo-space-m); height: 90%;">
   <vaadin-grid id="usersGrid" loading size="50" theme="column-borders" style="margin-bottom: var(--lumo-space-m);"></vaadin-grid>
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
