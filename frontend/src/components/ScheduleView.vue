<script>
import FullCalendar from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'
import timeGridPlugin from "@fullcalendar/timegrid"
import interactionPlugin from '@fullcalendar/interaction'
import fullCalendarLang from '@fullcalendar/core/locales/ko'

export default {
  components: {
    FullCalendar
  },
  data() {
    return {
      calendarOptions: {
        plugins: [ dayGridPlugin,timeGridPlugin, interactionPlugin ],
        initialView: 'dayGridMonth',
        dateClick: this.handleDateClick,
        events: [
          { title: 'event 1', date: '2023-11-11' },
          { title: 'event 2', date: '2019-04-02' }
        ],
        locale: fullCalendarLang,
        height: '100%',
        headerToolbar: {
          start: 'prev,next,today',
          center: 'title',
          end: 'dayGridMonth,timeGridWeek,timeGridDay'
        }
      }
    }
  },
  methods: {
    handleDateClick: function(arg) {
      alert('date click! ' + arg.dateStr)
    }
  }
}
</script>

<template>
  <div id="main-view" class="flex flex-col items-center bg-main_background bg-cover h-[93%] justify-center">
    <div class="h-[90%] flex flex-col w-5/6 bg-white rounded p-6 border-yellow-500 border-t-[5px]">
      <FullCalendar :options="calendarOptions" />
    </div>
  </div>
</template>
