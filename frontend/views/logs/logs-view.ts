import '@polymer/iron-icon/iron-icon';
import '@vaadin/vaadin-grid/all-imports';
import '@vaadin/vaadin-icons';
import '@vaadin/vaadin-lumo-styles/all-imports';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';
import '@vaadin/vaadin-ordered-layout/vaadin-vertical-layout';
import { customElement, html, LitElement } from 'lit-element';

@customElement('logs-view')
export class LogsView extends LitElement {
  createRenderRoot() {
    // Do not use a shadow root
    return this;
  }

  render() {
    return html`<vaadin-grid id="grid" theme="no-border no-row-borders"> </vaadin-grid>`;
  }
}
