import { LitElement, html, css, customElement } from 'lit-element';
import '@vaadin/vaadin-board/src/vaadin-board.js';
import '@vaadin/vaadin-board/src/vaadin-board-row.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js';

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
<vaadin-board id="boardLayout">
 <vaadin-board-row>
  <div class="wrapper">
   <div class="card space-m">
    <span theme="badge" style="width: 100%;">Water sensor</span>
    <h2 id="titleNameField" style="margin-bottom: var(--lumo-space-m); margin-top: var(--lumo-space-m); margin-left: var(--lumo-space-s);"></h2>
    <span class="secondary-text" id="ownerSpanField" style="width: 100%; margin-left: var(--lumo-space-s); margin-top: var(--lumo-space-m);">Owner: </span>
    <span id="createdField" style="margin-left: var(--lumo-space-s); margin-top: var(--lumo-space-m);">Created: </span>
   </div>
  </div>
  <div class="wrapper">
   <div class="card space-m">
    <span theme="badge error" style="width: 100%; margin-left: var(--lumo-space-xs);">Hardware info</span>
    <span style="width: 100%; margin-top: var(--lumo-space-m); margin-left: var(--lumo-space-s);" id="hwNameField">HW name: </span>
    <span id="hwSerialCodeField" style="width: 100%; margin-left: var(--lumo-space-s); margin-top: var(--lumo-space-xs); margin-bottom: var(--lumo-space-xs);">Span</span>
    <div id="onlineStatusDiv" style="margin-left: var(--lumo-space-s);">
      Online status: 
    </div>
    <vaadin-horizontal-layout theme="spacing" style="margin-left: var(--lumo-space-s);">
     <div id="activeStatusHwField" style="align-self: center;">
       Signal power: 
     </div>
     <div id="signalPowerHwField"></div>
    </vaadin-horizontal-layout>
    <div id="hardwareStatusActualizedField" style="margin-left: var(--lumo-space-s);"></div>
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
    <vaadin-horizontal-layout id="horizontalLayoutAboveChart" style="align-items: center; width: 100%; flex-direction: row-reverse;">
     <vaadin-horizontal-layout theme="spacing" style="width: 33%; justify-content: flex-end;">
      <vaadin-date-picker id="consumptionDatePicker" style="flex-shrink: 1; margin-right: var(--lumo-space-xl);" label="Date" helper-text=""></vaadin-date-picker>
     </vaadin-horizontal-layout>
     <vaadin-horizontal-layout theme="spacing" style="justify-content: center; width: 33%;">
      <div id="periodChangerChartDiv" style="flex-shrink: 0; margin-left: var(--lumo-space-xl);"></div>
     </vaadin-horizontal-layout>
     <vaadin-horizontal-layout theme="spacing" style="width: 33%; justify-content: flex-start;">
      <div id="typeOfChartDiv" style="flex-shrink: 0; margin-left: var(--lumo-space-xl); margin-top: var(--lumo-space-m);"></div>
     </vaadin-horizontal-layout>
    </vaadin-horizontal-layout>
    <vaadin-horizontal-layout style="width: 100%; justify-content: center;">
     <h4 id="chartTitle" style="align-self: center;">Heading 4</h4>
    </vaadin-horizontal-layout>
    <vaadin-horizontal-layout style="justify-content: center; align-items: center; width: 100%;">
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
 <vaadin-board-row id="boardRowBottom">
  <div id="scrollDownDiv">
   Yo
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
