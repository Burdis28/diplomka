import '@vaadin/vaadin-combo-box';
import '@vaadin/vaadin-date-picker';
import '@vaadin/vaadin-grid-pro/vaadin-grid-pro-edit-column';
import '@vaadin/vaadin-grid/all-imports';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';
import '@vaadin/vaadin-text-field';
import { customElement, html, LitElement } from 'lit-element';
import '@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';
import '@vaadin/vaadin-grid/src/vaadin-grid.js';
import '@vaadin/vaadin-button/src/vaadin-button.js';

@customElement('hardwares-view')
export class HardwaresView extends LitElement {
  createRenderRoot() {
    // Do not use a shadow root
    return this;
  }

  render() {
    return html`
<vaadin-vertical-layout style="width: 100%; height: 95%;">
 <div id="overallDiv" class="overallDiv" style="align-self: center; flex-grow: 1; height: 100%; width: 95%;">
  <vaadin-vertical-layout id="controlPanelLayout">
   <div style="flex-grow: 0; align-self: center;">
    <vaadin-horizontal-layout>
     <h2 style="align-self: center;">Hardware management</h2>
    </vaadin-horizontal-layout>
   </div>
  </vaadin-vertical-layout>
  <vaadin-vertical-layout theme="spacing" style="margin: var(--lumo-space-m); height: 80%; align-items: center;" id="gridLayout">
   <vaadin-grid id="hardwareGrid" theme="column-borders" column-reordering-allowed style="height: 100%;"></vaadin-grid>
   <vaadin-button id="createHwButton" style="align-self: center; margin-top: var(--lumo-space-m);" theme="primary">
     Create new HW 
   </vaadin-button>
  </vaadin-vertical-layout>
 </div>
</vaadin-vertical-layout>
`;
  }
}
