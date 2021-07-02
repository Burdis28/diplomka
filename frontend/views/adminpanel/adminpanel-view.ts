import { customElement, html, LitElement } from 'lit-element';

@customElement('adminpanel-view')
export class AdminpanelView extends LitElement {
  createRenderRoot() {
    // Do not use a shadow root
    return this;
  }

  render() {
    return html`
<vaadin-text-field id="name" label="Your name"></vaadin-text-field>
<vaadin-button id="sayHello">
 Say hello
</vaadin-button>
`;
  }
}
