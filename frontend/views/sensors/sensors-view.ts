import '@vaadin/vaadin-combo-box';
import '@vaadin/vaadin-date-picker';
import '@vaadin/vaadin-grid/all-imports';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';
import '@vaadin/vaadin-text-field';
import { customElement, html, LitElement } from 'lit-element';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js';
import '@vaadin/vaadin-grid';
import '@vaadin/vaadin-button/src/vaadin-button.js';

@customElement('sensors-view')
export class SensorsView extends LitElement {
  createRenderRoot() {
    // Do not use a shadow root
    return this;
  }

  render() {
    return html`
<vaadin-vertical-layout style="width: 100%; height: 100%;" theme="spacing">
 <div class="overallDiv" style="align-self: center; flex-grow: 1; height: 100%; width: 95%;">
  <vaadin-vertical-layout id="controlPanelLayout">
   <div style="align-self: center; flex-shrink: 1; flex-grow: 0;">
    <vaadin-horizontal-layout style="align-items: center; margin-top: -2em;">
     <h2 style="align-self: center;">Sensors management</h2>
    </vaadin-horizontal-layout>
   </div>
   <vaadin-button theme="primary" style="align-self: center;" id="createSensorButton">
     Create new sensor 
   </vaadin-button>
  </vaadin-vertical-layout>
  <vaadin-vertical-layout id="gridLayout" style="margin: var(--lumo-space-m); height: 90%; margin-top: var(--lumo-space-m);" theme="spacing">
   <vaadin-grid id="grid" theme="column-borders" page-size="10" style="margin-bottom: var(--lumo-space-m);"></vaadin-grid>
  </vaadin-vertical-layout>
 </div>
</vaadin-vertical-layout>
`;
  }
}
