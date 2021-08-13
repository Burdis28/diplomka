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
