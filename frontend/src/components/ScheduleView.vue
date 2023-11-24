<script>
import FullCalendar from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'
import timeGridPlugin from "@fullcalendar/timegrid"
import interactionPlugin from '@fullcalendar/interaction'
import fullCalendarLang from '@fullcalendar/core/locales/ko'
import {getCurrentInstance, inject, onMounted, ref} from "vue";
import PopupWindow from "@/components/PopupWindow.vue";
import writeEditor from "@/components/WriteEditor.vue";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import Multiselect from "vue-multiselect";
import {useTaskStore} from "@/stores/task";
import {useUserStore} from "@/stores/user";
import ToggleSwitch from "@/components/ToggleSwitch.vue";

export default {
    components: {
        ToggleSwitch,
        Multiselect,
        FontAwesomeIcon,
        writeEditor,
        PopupWindow,
        FullCalendar,
    },
    data() {
        return {
            task: {
                isShow: false,
                value: {
                    title: '',
                    category_code: '00',
                    content: '',
                    is_all_day: true,
                    start_date: null,
                    end_date: null,
                    start_time: null,
                    end_time: null,
                    date_time: [
                        new Date(),
                        new Date()
                    ],
                    bg_color: '#3688D8',
                    text_color: '#ffffff',
                },
                clickDateStr: null,
                clickDate: null
            },
            share: {
                isShow: false,
                team: [
                    { name: '김철득', teamValue: 1},
                    { name: '이진성', teamValue: 2},
                    { name: '박조상', teamValue: 3},
                    { name: '4444', teamValue: 4},
                    { name: '5555', teamValue: 5},
                    { name: '6666', teamValue: 6},
                    { name: '7777777', teamValue: 7},
                ],
                tmpTeamValue:[],
                teamValue:[],
                tmpCaption:'null',
                caption: null,
            },
            calendarOptions: {
                wheelEndTimeout: null,
                plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],
                initialView: 'dayGridMonth',
                dateClick: this.handleDateClick,
                eventDidMount: function(info) {
                    this.utils.tippy(info.el, {
                        content: info.event.extendedProps.content,
                        allowHTML: true,
                        theme: 'light',
                    });
                    if(info.event._def.extendedProps.is_share){
                        console.log(info.el);
                        info.el.style = 'outline: 3px solid #ff6600 !important';
                    }
                }.bind(this),
                eventDrop: function(info) {
                    const start = this.utils.dateToUnix(info.event._instance.range.start);
                    const end = this.utils.dateToUnix(info.event._instance.range.end);

                    const _tmp = Object.assign({}, info.event._def);
                    let data = Object.assign({}, info.event._def.extendedProps);

                    data.content = data.oriContent;
                    data.title = _tmp.title;
                    data.is_all_day = _tmp.allDay;

                    if(data.is_all_day){
                        let _start = info.event._instance.range.start;
                        _start.setHours(0,0,0,0);
                        data.start_date_time = this.utils.dateToUnix(_start);
                        data.end_date_time = null;
                    }else{
                        data.start_date_time = start;
                        data.end_date_time = end;
                    }
                    this.dragTask(data);
                }.bind(this),
                eventResize: function(info) {
                    const start = this.utils.dateToUnix(info.event._instance.range.start);
                    const end = this.utils.dateToUnix(info.event._instance.range.end);

                    const _tmp = Object.assign({}, info.event._def);
                    let data = Object.assign({}, info.event._def.extendedProps);

                    data.content = data.oriContent;
                    data.title = _tmp.title;
                    data.is_all_day = _tmp.allDay;

                    if(data.is_all_day){
                        let _start = info.event._instance.range.start;
                        _start.setHours(0,0,0,0);
                        data.start_date_time = this.utils.dateToUnix(_start);
                        if(info.event._instance.range.start.getDate() != info.event._instance.range.end.getDate()){
                            let _end = info.event._instance.range.end;
                            _end.setHours(23,59,0,0);
                            data.end_date_time = this.utils.dateToUnix(_end);
                            data.is_all_day = false;
                        }else{
                            data.end_date_time = null;
                        }
                    }else{
                        data.start_date_time = start;
                        data.end_date_time = end;
                    }
                    this.dragTask(data);
                    // console.log(info);
                }.bind(this),
                eventContent: function( info ) {
                    return {html: info.event.title};
                },
                locale: fullCalendarLang,
                height: '100%',
                editable: true,
                events: [],
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
        const utils = inject('utils');
        const userStore = useUserStore();
        const user = userStore.userInfo;
        const taskStore = useTaskStore();
        const tasks = ref([]);
        const content = ref('');
        const instance = getCurrentInstance();
        const mainDatePicker = ref({
            date: '',
            type: 'date',
        });

        const getTasks = async () => {
            if (instance.refs.fullCalendar) {
                const fullCalendarApi = instance.refs.fullCalendar.getApi();
                let startDate = fullCalendarApi.currentData.dateProfile.renderRange.start;
                let endDate = fullCalendarApi.currentData.dateProfile.renderRange.end;
                startDate = new Date(startDate.getTime() + (startDate.getTimezoneOffset() * 60000));
                endDate = new Date(endDate.getTime() + (endDate.getTimezoneOffset() * 60000));

                const data = {
                    user_seq: user.seq,
                    startDatetime: instance.appContext.config.globalProperties.utils.dateToUnix(startDate),
                    endDatetime: instance.appContext.config.globalProperties.utils.dateToUnix(endDate)
                }

                try {
                    await taskStore.getTasks(data);
                    if (taskStore?.dataResponse.status === 200) {
                        instance.data.calendarOptions.events = taskStore.taskList;
                    } else {
                        instance.appContext.config.globalProperties.utils.msgError(this.dataResponse.data || instance.appContext.config.globalProperties.utils.normalErrorMsg);
                    }
                    tasks.value = taskStore.taskList;
                } catch (error) {
                    console.error(error);
                    instance.appContext.config.globalProperties.utils.msgError((error?.response?.data) || instance.appContext.config.globalProperties.utils.normalErrorMsg);
                }
            }
        }

        onMounted(async () => {
            const fullCalendarApi = instance.refs.fullCalendar.getApi();
            const datePicker = instance.refs.datePicker;
            const fcTodayButton = document.querySelector(".fc-today-button");
            const fcToolbar = document.querySelector(".fc-toolbar-title").closest("div");
            fcToolbar.classList.add("cursor-pointer");
            fcToolbar.classList.add("p-2");
            fcToolbar.classList.add("rounded");
            const fcToolbarType = document.querySelector(".fc-dayGridMonth-button").closest("div");
            fcToolbarType.addEventListener('click', () => {
               getTasks();
            });

            window.addEventListener('keyup', (e) => {
                let flag = false;

                if(e.key === 'ArrowLeft'){
                    flag = true;
                    fullCalendarApi.prev();
                }else if(e.key === 'ArrowRight'){
                    flag = true;
                    fullCalendarApi.next();
                }
                if(flag){
                    if (instance.data.calendarOptions.wheelEndTimeout)
                        clearTimeout(instance.data.calendarOptions.wheelEndTimeout);

                    instance.data.calendarOptions.wheelEndTimeout = setTimeout(async () => {
                        await getTasks();
                    }, 500);
                }
            })
            fcTodayButton.addEventListener('click', () => {
                getTasks();
            });
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
            await getTasks();
        })

        return {
            mainDatePicker,
            content,
            user,
            getTasks,
            tasks,
            utils
        }
    },
    methods: {
        handleDateClick: function (arg) {
            const week = ['일', '월', '화', '수', '목', '금', '토'];
            this.task.clickDate = Math.floor(arg.date.getTime() / 1000);
            this.task.clickDateStr = arg.dateStr + ' (' + week[arg.date.getDay()] + ')';
            this.task.isShow = true;
            const B = new Date(arg.date.getTime());
            B.setHours(23, 59, 0, 0);
            this.task.value.date_time[0] = arg.date;
            this.task.value.date_time[1] = B;
        },
        handleDatePicker() {
            let calendarApi = this.$refs.fullCalendar.getApi()
            calendarApi.gotoDate(this.mainDatePicker.date);

            this.getTasks();
        },
        test2() {
            const html = document.querySelector("html");
            html.classList.toggle("dark");
        },
        async addTask() {
            if(this.task.value.title == null ||this.task.value.title === ''){
                this.utils.msgError("제목을 입력 해 주세요.");
                return;
            }

            const data = this.task.value;
            const shareTeamValue = this.share.teamValue;
            const shareUserSeq = [];
            shareTeamValue.forEach((team) => {
                shareUserSeq.push(team.teamValue);
            });
            data.shared_user_seq = shareUserSeq;
            data.caption = this.share.caption;
            data.user_seq = this.user.seq;
            if(this.task.value.is_all_day){
                this.task.value.start_date_time = this.task.clickDate;
            }else{
                this.task.value.start_date_time = Math.floor(this.task.value.date_time[0].getTime() / 1000);
                this.task.value.end_date_time = Math.floor(this.task.value.date_time[1].getTime() / 1000);
            }

            try {
                const taskStore = useTaskStore();
                await taskStore.addTask(data);
                if (taskStore?.dataResponse.status === 200) {
                    this.cancelTask();
                    this.utils.notify.success("등록되었습니다.", "등록 완료!");
                    await this.getTasks();
                } else {
                    this.utils.msgError(taskStore.dataResponse.data || this.utils.normalErrorMsg);
                }
            } catch (error) {
                this.utils.msgError((error?.response?.data) || this.utils.normalErrorMsg);
            }
        },
        async dragTask(data) {
            try {
                const taskStore = useTaskStore();
                await taskStore.updateTask(data);
                if (taskStore?.dataResponse.status === 200) {
                    this.utils.notify.success("변경되었습니다.", "등록 완료!");
                } else {
                    this.utils.msgError(taskStore.dataResponse.data || this.utils.normalErrorMsg);
                }
            } catch (error) {
                this.utils.msgError((error?.response?.data) || this.utils.normalErrorMsg);
            }
        },
        cancelTask() {
            this.task.value.title = null;
            this.$refs.taskForm.reset();
            this.$refs.taskContent.clearEditor();
            this.task.value.content = null;
            this.task.isShow = false;
        },
        addTag (newTag) {
            const tag = {
                name: newTag,
                teamValue: newTag.substring(0, 2) + Math.floor((Math.random() * 10000000))
            }
            this.options.push(tag)
            this.value.push(tag)
        },
        openShare() {
            this.share.tmpTeamValue = this.share.teamValue;
            this.share.tmpCaption = this.share.caption;
            this.share.isShow = true;
        },
        addShare() {
            this.share.teamValue = this.share.tmpTeamValue;
            this.share.caption = this.share.tmpCaption;
            this.share.isShow = false;
        },
        cancelShare() {
            this.share.isShow = false;
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
                    :events="tasks"
                    ref="fullCalendar"
            />
            <el-date-picker
                    ref="datePicker"
                    class="absolute z-20 border-0 outline-0 left-[calc(48%)] top-[4rem]"
                    id="date-picker"
                    v-model="mainDatePicker.date"
                    format="YYYY년 MM월"
                    :type="mainDatePicker.type"
                    @change="handleDatePicker"
            />
            <div class="flex absolute outline-0 right-[calc(18%)] top-[2rem] z-10">
                <div class="flex">
                    <Multiselect
                            class="w-[15rem] h-[3rem]"
                            v-model="share.tmpTeamValue"
                            track-by="teamValue"
                            label="name"
                            placeholder="분류"
                            tag-placeholder="분류를 선택해주세요."
                            :options="share.team"
                            :multiple="true"
                            :taggable="true"
                            @tag="addTag"
                            :close-on-select="false"
                    />
                </div>
            </div>
        </div>
        <PopupWindow
                :show="task.isShow"
                :title="'일정 등록'"
                :subTitle="task.clickDateStr"
                :widthClass="'w-[70%]'"
                :heightClass="'h-[45rem]'"
                :alignClass="'justify-start'"
        >
            <button class="absolute" @click="test2">
                테스트
            </button>
            <form ref="taskForm" @submit.prevent="addTask">
                <div class="flex flex-col items-center p-5 w-full h-full">
                    <p class="flex flex-col items-start w-full mb-3">
                        <span class="text-lg">
                            제목
                            <b class="text-red-500">*</b>
                        </span>
                        <input
                                v-model="task.value.title"
                                placeholder="제목을 입력해주세요."
                                class="rounded text-black px-3 py-1.5 text-xl border border-gray-300 w-full "
                                type="text">
                    </p>
                    <div class="flex items-start w-full mb-3 justify-between">
                        <div class="flex flex-col items-start w-5/6">
                            <label class="flex items-center mb-1">
                                <span class="flex items-center justify-center">하루 종일</span>
                                <ToggleSwitch v-model="task.value.is_all_day"></ToggleSwitch>
                            </label>
                            <VueDatePicker v-model="task.value.date_time"
                                           range
                                           format='yyyy년 MM월 dd일 HH시 mm분'
                                           locale="ko"
                                           :disabled="task.value.is_all_day"
                            ></VueDatePicker>
                        </div>
                        <div class="flex flex-col justify-end items-center w-1/6 h-full">
                            <label class="w-4/6 flex justify-between">
                                <span>배경 색 : </span>
                                <el-color-picker v-model="task.value.bg_color" />
                            </label>
                            <label class="w-4/6 flex justify-between">
                                <span>텍스트 색 : </span>
                                <el-color-picker v-model="task.value.text_color" />
                            </label>
                        </div>
                    </div>
                    <p class="flex w-full h-[25rem] mb-3 rounded">
                        <writeEditor ref="taskContent" class="w-full max-h-[25rem]" v-model="task.value.content"/>
                    </p>

                    <div class="flex justify-between text-white w-full">
                        <div>
                            <button class="px-5 py-3 bg-orange-500 rounded text-sm hover:bg-orange-400 mr-3 relative"
                                    type="button"
                                    @click="openShare">
                                <span class="flex justify-center items-center">
                                    <b  v-if="share.teamValue.length > 0"
                                        class="absolute -right-3 -top-2 flex justify-center items-center w-6 h-6 mr-1 rounded-full bg-blue-500">
                                        {{share.teamValue.length}}
                                    </b>
                                    <font-awesome-icon class="mr-1" icon="fa-user-group"/>
                                    공유하기
                                </span>
                            </button>
                        </div>
                        <div>
                            <button class="px-7 py-3 bg-blue-600 rounded text-sm hover:bg-blue-500 mr-3"
                                    type="button"
                                    @click="addTask">
                                <span class="">등록</span>
                            </button>
                            <button class="px-7 py-3 bg-gray-500 rounded text-sm hover:bg-gray-400"
                                    type="button"
                                    @click="cancelTask">취소
                            </button>
                        </div>
                    </div>

                </div>
                <PopupWindow
                        :show="share.isShow"
                        @close="share.isShow = false"
                        :title="'공유하기'"
                        :widthClass="'w-[50%]'"
                        :heightClass="'h-[30rem]'"
                        :alignClass="'justify-start'"
                >
                    <div class="flex flex-col items-center p-5 w-full h-full">
                        <p class="flex flex-col items-start w-full mb-3">
                            <span class="text-lg">캡션</span>
                            <input
                                    v-model="share.tmpCaption"
                                    placeholder="캡션"
                                    class="rounded text-black px-3 py-1.5 text-md border border-gray-300 w-full "
                                    type="text" maxlength="200">
                        </p>
                        <p class="flex flex-col items-start w-full mb-3 h-full">
                            <Multiselect
                                    v-model="share.tmpTeamValue"
                                    track-by="teamValue"
                                    label="name"
                                    placeholder="공유 대상을 선택해주세요."
                                    tag-placeholder="공유 대상을 선택해주세요."
                                    :options="share.team"
                                    :multiple="true"
                                    :taggable="true"
                                    @tag="addTag"
                                    :close-on-select="false"
                            />
                        </p>
                        <div class="flex justify-center text-white w-full">
                            <button class="px-7 py-3 bg-blue-600 rounded text-sm hover:bg-blue-500 mr-3"
                                    type="button"
                                    @click="addShare">
                                <span class="">확인</span>
                            </button>
                            <button class="px-7 py-3 bg-gray-500 rounded text-sm hover:bg-gray-400"
                                    type="button"
                                    @click="cancelShare">취소
                            </button>
                        </div>

                    </div>
                </PopupWindow>
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

.fc-day:not(.fc-col-header-cell):hover {
    cursor: pointer;
    background: #cdffff;
}

.fc-day-sat .fc-daygrid-day-number,
.fc-day-sun .fc-daygrid-day-number
{
    color: red;
}

.dp__disabled {
    color: #e4e4e4 !important;
}
</style>
