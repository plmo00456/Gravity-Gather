<script>
import FullCalendar from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'
import timeGridPlugin from "@fullcalendar/timegrid"
import interactionPlugin from '@fullcalendar/interaction'
import fullCalendarLang from '@fullcalendar/core/locales/ko'
import {getCurrentInstance, onMounted, ref} from "vue";
import PopupWindow from "@/components/PopupWindow.vue";

export default {
    components: {
        PopupWindow,
        FullCalendar,
    },
    data() {
        return {
            calendarOptions: {
                plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],
                initialView: 'dayGridMonth',
                dateClick: this.handleDateClick,
                events: [
                    {title: 'event 1', date: '2023-11-15'},
                    {title: 'event 2', start: '2023-11-20 10:00', end: '2023-11-21 10:40'},
                ],
                locale: fullCalendarLang,
                height: '100%',
                editable: true,
                headerToolbar: {
                    start: 'today',
                    center: 'title,datePickerButton',
                    end: 'dayGridMonth,timeGridWeek,timeGridDay'
                },
                customButtons: {
                    datePickerButton: {
                        icon: 'chevron-left',
                        class: 'bg-red-500',
                    }
                },
            },
        }
    },
    setup() {
        const instance = getCurrentInstance();
        const mainDatePicker = ref({
            date: '',
            type: 'date',
        });

        onMounted(() => {
            const fullCalendarApi = instance.refs.fullCalendar.getApi();
            const datePicker = instance.refs.datePicker;
            const fcToolbar = document.querySelector(".fc-toolbar-title").closest("div");
            fcToolbar.classList.add("cursor-pointer");
            fcToolbar.classList.add("p-2");
            fcToolbar.classList.add("rounded");

            fcToolbar.addEventListener('click', () => {
                const currentType = fullCalendarApi.currentData.currentViewType;
                if (currentType === 'dayGridMonth') {
                    mainDatePicker.value.type = 'month';
                } else if (currentType === 'timeGridWeek') {
                    mainDatePicker.value.type = 'week';
                } else {
                    mainDatePicker.value.type = '';
                }

                datePicker.focus();
            });
        })

        return {
            mainDatePicker
        }
    },
    methods: {
        handleDateClick: function (arg) {
            console.log('date click! ' + arg.dateStr)
        },
        test() {
            let calendarApi = this.$refs.fullCalendar.getApi()
            calendarApi.gotoDate(this.mainDatePicker.date);
        },
        taskUpload() {

        }
    }
}
</script>

<template>
    <div id="main-view" class="flex flex-col items-center bg-main_background bg-cover h-[93%] justify-center">
        <div class="h-[90%] flex flex-col w-5/6 bg-white rounded p-6 border-yellow-500 border-t-[5px] relative">
            <FullCalendar
                    class="z-10"
                    id="calendar"
                    :options="calendarOptions"
                    ref="fullCalendar"
            />
            <el-date-picker
                    ref="datePicker"
                    class="absolute z-20 border-0 outline-0 left-[calc(48%)] top-[4rem]"
                    id="date-picker"
                    v-model="mainDatePicker.date"
                    format="YYYY년 MM월"
                    :type="mainDatePicker.type"
                    @change="test"
            />
        </div>
        <PopupWindow
                :show="false"
                :title="'일정 등록'"
                :widthClass="'w-[70%]'"
                :heightClass="'h-[45rem]'"
                :alignClass="'justify-start'"
        >
            <form @submit.prevent="taskUpload">
                <div class="flex flex-col items-center p-5">

                    <div class="flex w-full items-center h-[3.5rem] mb-1">
                        <p class="flex flex-col w-2/5 justify-center items-start">
                            <span class="flex w-full items-center font-semibold ml-1">
                              이름
                              <b class="ml-1 text-red-500">*</b>
                            </span>
                            <span>ㅎㅎ</span>
                        </p>
                        <p class="flex w-3/5 h-full items-center">
                            <input type="text" class="w-full border rounded border-gray-300 h-[2.5rem] px-2 py-1"
                                   maxlength="10">
                        </p>
                    </div>


                    <p class="flex justify-center text-white w-full">
                        <button class="px-5 py-2 bg-blue-600 rounded text-sm hover:bg-blue-500 mr-2 w-1/2 h-[3rem]"
                                type="submit">확인
                        </button>
                        <button class="px-5 py-2 bg-gray-500 rounded text-sm hover:bg-gray-400 ml-2 w-1/2 h-[3rem]"
                                type="button"
                                @click="myInfoShow = false">취소
                        </button>
                    </p>

                </div>
            </form>
        </PopupWindow>
    </div>
</template>

<style>
.el-input {
    width: 0 !important;
    height: 0 !important;
    overflow: hidden;
}

.fc-header-toolbar .fc-toolbar-chunk > div {
    display: flex;
}

.fc-header-toolbar .fc-button-group button {
    padding: 8px 20px;
}

.fc .fc-datePickerButton-button {
    margin-left: 10px;
    transform: rotate(270deg);
    background: #fff;
    color: #2c3e50;
    font-weight: bold;
    border: none;
    display: flex;
    justify-content: center;
    align-items: center;
}

.fc .fc-datePickerButton-button:hover {
    background: #eeeeee;
}

.fc-scroller::-webkit-scrollbar {
    width: 10px;
}

.fc-scroller::-webkit-scrollbar-thumb {
    background: #f1b4bb; /* 스크롤바 색상 */
    border-radius: 10px; /* 스크롤바 둥근 테두리 */
}

.fc-scroller::-webkit-scrollbar-track {
    background: rgba(220, 20, 60, .1); /*스크롤바 뒷 배경 색상*/
}

.fc-day:hover {
    cursor: pointer;
    background: #cdffff;
}
</style>