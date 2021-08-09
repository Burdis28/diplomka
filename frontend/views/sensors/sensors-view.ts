import '@vaadin/vaadin-combo-box';
import '@vaadin/vaadin-date-picker';
import '@vaadin/vaadin-grid-pro/vaadin-grid-pro-edit-column';
import '@vaadin/vaadin-grid/all-imports';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';
import '@vaadin/vaadin-text-field';
import { customElement, html, LitElement } from 'lit-element';
import '@vaadin/vaadin-grid-pro';

@customElement('sensors-view')
export class SensorsView extends LitElement {
  createRenderRoot() {
    // Do not use a shadow root
    return this;
  }

  render() {
    return html`
<vaadin-grid-pro id="grid" theme="no-border column-borders" page-size="10"></vaadin-grid-pro>
`;
  }
}
