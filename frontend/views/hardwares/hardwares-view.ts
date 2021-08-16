import '@vaadin/vaadin-combo-box';
import '@vaadin/vaadin-date-picker';
import '@vaadin/vaadin-grid-pro/vaadin-grid-pro-edit-column';
import '@vaadin/vaadin-grid/all-imports';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';
import '@vaadin/vaadin-text-field';
import { customElement, html, LitElement } from 'lit-element';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';
import '@vaadin/vaadin-grid/src/vaadin-grid.js';

@customElement('hardwares-view')
export class HardwaresView extends LitElement {
  createRenderRoot() {
    // Do not use a shadow root
    return this;
  }

  render() {
    return html`
<vaadin-vertical-layout theme="spacing" style="width: 100%; height: 100%;">
 <vaadin-grid id="hardwareGrid" column-reordering-allowed></vaadin-grid>
</vaadin-vertical-layout>
`;
  }
}
