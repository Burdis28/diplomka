import '@vaadin/vaadin-board';
import '@vaadin/vaadin-charts/vaadin-chart';
import '@vaadin/vaadin-lumo-styles/all-imports';
import { customElement, html, LitElement } from 'lit-element';

@customElement('dashboard-view')
export class DashboardView extends LitElement {
  createRenderRoot() {
    // Do not use a shadow root
    return this;
  }

  render() {
    return html`
      <vaadin-board>
        <vaadin-board-row>
          <div class="wrapper">
            <div class="card space-m">
              <span theme="badge">Users</span>
              <h2 class="primary-text" id="currentUsers"></h2>
              <span class="secondary-text">Current users in the app</span>
            </div>
          </div>
          <div class="wrapper">
            <div class="card space-m">
              <span theme="badge success">Events</span>
              <h2 class="success-text" id="numEvents"></h2>
              <span class="secondary-text">Events from the views</span>
            </div>
          </div>
          <div class="wrapper">
            <div class="card space-m">
              <span theme="badge error">Conversion</span>
              <h2 class="error-text" id="conversionRate"></h2>
              <span class="secondary-text">User conversion rate</span>
            </div>
          </div>
        </vaadin-board-row>
        <div class="wrapper">
          <div class="card">
            <vaadin-chart type="column" id="monthlyVisitors" title="Monthly visitors per city"></vaadin-chart>
          </div>
        </div>
        <vaadin-board-row>
          <div class="wrapper">
            <div class="card">
              <h3>Service health</h3>
              <vaadin-grid id="grid" theme="no-border"></vaadin-grid>
            </div>
          </div>
          <div class="wrapper">
            <div class="card">
              <vaadin-chart id="responseTimes" title="Response times"></vaadin-chart>
            </div>
          </div>
        </vaadin-board-row>
      </vaadin-board>
    `;
  }
}
