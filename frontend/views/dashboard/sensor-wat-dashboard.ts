import { LitElement, html, css, customElement } from 'lit-element';
import '@vaadin/vaadin-board/src/vaadin-board.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js';
import '@vaadin/vaadin-board/src/vaadin-board-row.js';

@customElement('sensor-wat-dashboard')
export class SensorWatDashboard extends LitElement {
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
<vaadin-board>
 <vaadin-board-row>
  <div class="wrapper">
   <div class="card space-m">
    <span theme="badge" style="width: 100%;">Electric sensor</span>
    <h2 id="titleNameField" style="margin-bottom: var(--lumo-space-m); margin-top: var(--lumo-space-m);"></h2>
    <span class="secondary-text" id="ownerSpanField" style="width: 100%;">Span</span>
   </div>
  </div>
  <div class="wrapper">
   <div class="card space-m">
    <span theme="badge success" style="width: 100%;">Current consumption</span>
    <div id="consumptionDivText" style="width: 100%; margin-top: var(--lumo-space-m); margin-bottom: var(--lumo-space-xs);">
      Consumption 
    </div>
    <vaadin-progress-bar id="consumptionProgressBar" value="20" max="50"></vaadin-progress-bar>
    <div id="priceDiv">
      Div 
    </div>
    <div id="actualizedDivField" style="margin-top: var(--lumo-space-s);">
      Actualized: 
    </div>
   </div>
  </div>
  <div class="wrapper">
   <div class="card space-m">
    <span theme="badge error" style="width: 100%;">Hardware info</span>
    <span style="width: 100%; margin-top: var(--lumo-space-m);" id="hwNameField">HW name: </span>
    <vaadin-horizontal-layout theme="spacing">
     <div id="activeStatusHwField" style="align-self: center;">
       Active status 
     </div>
     <vaadin-button theme="icon" aria-label="Add new" id="hardwareStatusButton"></vaadin-button>
    </vaadin-horizontal-layout>
    <div id="hardwareStatusActualizedField"></div>
   </div>
  </div>
  <div class="wrapper">
   <div class="card space-m">
    <span style="width: 100%;" theme="badge success">Monthly consumption limit</span>
    <div id="consumptionMonthDivText" style="margin-top: var(--lumo-space-m); margin-bottom: var(--lumo-space-xs);">
      This month consumption 
    </div>
    <vaadin-progress-bar id="monthLimitProgressBar" max="20" value="12"></vaadin-progress-bar>
    <div id="priceThisMonthDiv">
      Price: 
    </div>
   </div>
  </div>
 </vaadin-board-row>
 <vaadin-board-row>
  <div class="wrapper" style="flex-grow: 1;">
   <div class="card" id="dailyConsumptionDiv">
    <vaadin-date-picker id="consumptionDatePicker" style="margin: var(--lumo-space-s); margin-left: var(--lumo-space-s); padding-left: var(--lumo-space-m);" label="Date" helper-text="Today's date chosen by default"></vaadin-date-picker>
   </div>
  </div>
  <div class="wrapper">
   <div class="card">
    <h4 style="margin: var(--lumo-space-s); margin-top: var(--lumo-space-l); margin-left: var(--lumo-space-l); margin-right: var(--lumo-space-l); margin-bottom: var(--lumo-space-m);">Price of electricity consumed for a certain day </h4>
    <vaadin-date-picker label="Date" placeholder="Pick a date" id="priceForDayDatePicker" style="margin-left: var(--lumo-space-l);"></vaadin-date-picker>
    <div id="priceForADayDiv" style="margin-top: var(--lumo-space-l); margin-left: var(--lumo-space-l); margin-bottom: var(--lumo-space-l);"></div>
    <div style="padding: var(--lumo-space-l); padding-top: var(--lumo-space-xs);">
     <h4>Price of electricity consumed during a certain month and year</h4>
     <vaadin-horizontal-layout theme="spacing">
      <vaadin-text-field label="Choose a year" id="yearSelecterField" maxlength="4" required has-value></vaadin-text-field>
      <vaadin-select id="monthSelecter" label="Choose a month"></vaadin-select>
     </vaadin-horizontal-layout>
     <vaadin-horizontal-layout>
      <vaadin-button id="calculateBtn" style="align-self: flex-start; margin-top: var(--lumo-space-xl);" theme="primary first">
        Calculate month 
      </vaadin-button>
      <vaadin-button id="calculateWholeYear" style="margin-top: var(--lumo-space-xl); align-self: flex-start; margin-left: var(--lumo-space-m);" theme="secondary">
        Calculate whole year 
      </vaadin-button>
     </vaadin-horizontal-layout>
     <div id="priceForAMonth" style="margin-top: var(--lumo-space-l);"></div>
    </div>
   </div>
  </div>
 </vaadin-board-row>
 <vaadin-board-row>
  <div class="wrapper">
   <div class="card">
    <div id="monthlyChartDiv" style="margin: var(--lumo-space-l);"></div>
    <vaadin-chart title="Consumption prices for past 30 days" id="monthlyChart" style="margin-top: var(--lumo-space-l);"></vaadin-chart>
   </div>
  </div>
 </vaadin-board-row>
</vaadin-board>
`;
  }

  // Remove this method to render the contents of this view inside Shadow DOM
  createRenderRoot() {
    return this;
  }
}
