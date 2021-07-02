import '@polymer/iron-icon';
import '@vaadin/vaadin-ordered-layout';
import { customElement, html, LitElement } from 'lit-element';

@customElement('card-list-item')
export class CardListItem extends LitElement {
  createRenderRoot() {
    // Do not use a shadow root
    return this;
  }

  render() {
    return html`
      <vaadin-horizontal-layout theme="spacing-s" class="card">
        <img id="image" />
        <vaadin-vertical-layout>
          <vaadin-horizontal-layout theme="spacing-s" class="header">
            <span class="name" id="name"></span>
            <span class="date" id="date"></span>
          </vaadin-horizontal-layout>
          <span class="post" id="post"></span>
          <vaadin-horizontal-layout theme="spacing-s" class="actions">
            <iron-icon icon="vaadin:heart"></iron-icon>
            <span class="likes" id="likes"></span>
            <iron-icon icon="vaadin:comment"></iron-icon>
            <span class="comments" id="comments"></span>
            <iron-icon icon="vaadin:connect"></iron-icon>
            <span class="shares" id="shares"></span>
          </vaadin-horizontal-layout>
        </vaadin-vertical-layout>
      </vaadin-horizontal-layout>
    `;
  }
}
