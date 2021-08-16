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
    <span theme="badge" style="width: 100%;">Water sensor</span>
    <h2 id="titleNameField" style="margin-bottom: var(--lumo-space-m); margin-top: var(--lumo-space-m); margin-left: var(--lumo-space-l);"></h2>
    <span class="secondary-text" id="ownerSpanField" style="width: 100%; margin-left: var(--lumo-space-l);">Owner: </span>
    <span id="createdField" style="margin-left: var(--lumo-space-l);">Created: </span>
   </div>
  </div>
  <div class="wrapper">
   <div class="card space-m">
    <span theme="badge error" style="width: 100%;">Hardware info</span>
    <span style="width: 100%; margin-top: var(--lumo-space-m); margin-left: var(--lumo-space-l);" id="hwNameField">HW name: </span>
    <div id="onlineStatusDiv" style="margin-left: var(--lumo-space-l);">
      Online status: 
    </div>
    <vaadin-horizontal-layout theme="spacing" style="margin-left: var(--lumo-space-l);">
     <div id="activeStatusHwField" style="align-self: center;">
       Signal power: 
     </div>
     <div id="signalPowerHwField"></div>
    </vaadin-horizontal-layout>
    <div id="hardwareStatusActualizedField" style="margin-left: var(--lumo-space-l);"></div>
   </div>
  </div>
 </vaadin-board-row>
 <vaadin-board-row>
  <div class="wrapper">
   <div class="card space-m">
    <span theme="badge success" style="width: 100%;">Current consumption</span>
    <div id="consumptionDivText" style="width: 100%; margin-top: var(--lumo-space-m); margin-bottom: var(--lumo-space-xs);">
      Consumption 
    </div>
    <vaadin-progress-bar id="consumptionProgressBar" value="20" max="50"></vaadin-progress-bar>
    <div id="actualizedDivField" style="margin-top: var(--lumo-space-s);">
      Actualized: 
    </div>
   </div>
  </div>
 </vaadin-board-row>
 <vaadin-board-row>
  <div class="wrapper">
   <div class="card space-m">
    <span theme="badge success" style="width: 100%;">Today consumption</span>
    <div id="consumptionTodayDivText" style="width: 100%; margin-top: var(--lumo-space-m); margin-bottom: var(--lumo-space-xs);">
      Today consumption 
    </div>
    <vaadin-progress-bar id="todayLimitProgressBar" value="6" max="10"></vaadin-progress-bar>
    <div id="priceTodayDiv">
      Div 
    </div>
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
    <vaadin-horizontal-layout id="horizontalLayoutAboveChart" style="justify-content: center; align-items: center; width: 100%;">
     <div id="typeOfChartDiv"></div>
     <div id="periodChangerChartDiv"></div>
     <vaadin-date-picker id="consumptionDatePicker" style="margin: var(--lumo-space-s); margin-left: var(--lumo-space-s); padding-left: var(--lumo-space-m); flex-shrink: 1;" label="Date" helper-text="Today's date chosen by default"></vaadin-date-picker>
     <vaadin-button id="previousButton" style="margin: var(--lumo-space-m); margin-top: var(--lumo-space-m); flex-shrink: 1; margin-right: var(--lumo-space-s);">
       Previous 
     </vaadin-button>
     <vaadin-button id="nextButton" style="margin: var(--lumo-space-m); margin-top: var(--lumo-space-m); flex-shrink: 1; margin-left: var(--lumo-space-s);">
       Next 
     </vaadin-button>
    </vaadin-horizontal-layout>
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
