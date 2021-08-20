import { LitElement, html, css, customElement } from 'lit-element';
import '@vaadin/vaadin-board/src/vaadin-board.js';
import '@vaadin/vaadin-board/src/vaadin-board-row.js';
import '@vaadin/vaadin-button/src/vaadin-button.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js';
import '@vaadin/vaadin-date-picker/src/vaadin-date-picker.js';
import '@vaadin/vaadin-progress-bar/src/vaadin-progress-bar.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';

@customElement('sensor-el-dashboard')
export class SensorElDashboard extends LitElement {
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
  <div class="wrapper" style="flex-grow: 1;">
   <div class="card space-m">
    <span theme="badge" style="width: 100%;">Electric sensor</span>
    <h2 id="titleNameField" style="margin-bottom: var(--lumo-space-m); margin-top: var(--lumo-space-m); margin-left: var(--lumo-space-s);"></h2>
    <span id="ownerSpanField" style="width: 100%; margin-left: var(--lumo-space-s); margin-top: var(--lumo-space-m);">Span</span>
    <span id="createdField" style="margin-left: var(--lumo-space-s); margin-top: var(--lumo-space-m);">Span</span>
   </div>
  </div>
  <div class="wrapper" style="flex-shrink: 1; flex-grow: 1;">
   <div class="card space-m">
    <span theme="badge error" style="width: 100%;">Hardware info</span>
    <span style="width: 100%; margin-top: var(--lumo-space-m); margin-left: var(--lumo-space-s);" id="hwNameField">HW name: </span>
    <span id="hwSerialCodeField" style="width: 100%; margin-left: var(--lumo-space-s); margin-top: var(--lumo-space-xs); margin-bottom: var(--lumo-space-xs);">Span</span>
    <div id="onlineStatusDiv" style="margin-left: var(--lumo-space-s);">
      Online status: 
    </div>
    <vaadin-horizontal-layout theme="spacing" style="align-items: center; margin-left: var(--lumo-space-s);">
     <div>
       Signal power: 
     </div>
     <div id="signalPowerHwField" style="align-self: center;"></div>
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
    <vaadin-progress-bar id="consumptionProgressBar" max="" min=""></vaadin-progress-bar>
    <div id="actualizedDivField" style="margin-top: var(--lumo-space-s);">
      Actualized: 
    </div>
   </div>
  </div>
 </vaadin-board-row>
 <vaadin-board-row>
  <div class="wrapper">
   <div class="card space-m" id="todayConsumptionDiv">
    <span style="width: 100%;" theme="badge success">Today consumption</span>
    <vaadin-progress-bar id="todayLimitProgressBar" max="10" value="6"></vaadin-progress-bar>
    <vaadin-horizontal-layout>
     <vaadin-vertical-layout id="gaugeLayout"></vaadin-vertical-layout>
     <vaadin-vertical-layout style="justify-content: center; align-items: center; width: 100%;">
      <div id="consumptionTodayDivText" style="margin-top: var(--lumo-space-m); margin-bottom: var(--lumo-space-xs);">
        Today consumption 
      </div>
      <div id="priceTodayDiv">
        Div 
      </div>
     </vaadin-vertical-layout>
    </vaadin-horizontal-layout>
   </div>
  </div>
  <div class="wrapper">
   <div class="card space-m" id="monthlyConsumptionDiv">
    <span style="width: 100%;" theme="badge success">Monthly consumption</span>
    <vaadin-progress-bar id="monthLimitProgressBar" max="20" value="12"></vaadin-progress-bar>
    <vaadin-horizontal-layout>
     <vaadin-vertical-layout id="gaugeMonthLayout"></vaadin-vertical-layout>
     <vaadin-vertical-layout style="width: 100%; align-items: center; justify-content: center;">
      <div id="consumptionMonthDivText" style="margin-top: var(--lumo-space-m); margin-bottom: var(--lumo-space-xs);">
        This month consumption 
      </div>
      <div id="priceThisMonthDiv">
        Price: 
      </div>
     </vaadin-vertical-layout>
    </vaadin-horizontal-layout>
   </div>
  </div>
 </vaadin-board-row>
 <vaadin-board-row>
  <div class="wrapper" style="flex-grow: 1;">
   <div class="card" id="dailyConsumptionDiv">
    <vaadin-horizontal-layout id="horizontalLayoutAboveChart" style="align-items: center; width: 100%;">
     <vaadin-horizontal-layout theme="spacing" style="width: 33%; justify-content: flex-start;">
      <div id="typeOfChartDiv" style="margin-left: var(--lumo-space-xl); margin-top: var(--lumo-space-m);"></div>
     </vaadin-horizontal-layout>
     <vaadin-horizontal-layout theme="spacing" style="width: 33%; justify-content: center;">
      <div id="periodChangerChartDiv" style="margin-top: var(--lumo-space-m);"></div>
     </vaadin-horizontal-layout>
     <vaadin-horizontal-layout theme="spacing" style="width: 33%; justify-content: flex-end;">
      <vaadin-date-picker id="consumptionDatePicker" style="flex-shrink: 1; margin-right: var(--lumo-space-xl);" label="Date"></vaadin-date-picker>
     </vaadin-horizontal-layout>
    </vaadin-horizontal-layout>
    <vaadin-horizontal-layout theme="spacing" style="width: 100%; justify-content: center;">
     <h4 id="chartTitle">Heading 4</h4>
    </vaadin-horizontal-layout>
    <vaadin-horizontal-layout theme="spacing" style="justify-content: center; width: 100%;">
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
