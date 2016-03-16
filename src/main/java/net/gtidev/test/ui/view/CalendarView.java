package net.gtidev.test.ui.view;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.components.calendar.CalendarComponentEvents;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.handler.BasicDateClickHandler;
import com.vaadin.ui.components.calendar.handler.BasicWeekClickHandler;
import com.vaadin.ui.themes.ValoTheme;
import net.gtidev.test.EventProvider;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.text.DateFormatSymbols;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

@SpringView
public class CalendarView extends GridLayout implements View {

  /**
   * This Gregorian calendar is used to control dates and time inside of this
   * test application.
   */
  @Autowired
  private GregorianCalendar calendar;

  @Autowired
  private EventProvider eventProvider;

  private static final long serialVersionUID = -5436777475398410597L;

  private static final String DEFAULT_ITEMID = "DEFAULT";

  private enum Mode {
    MONTH, WEEK, DAY;
  }

  /**
   * Target calendar component that this test application is made for.
   */
  private Calendar calendarComponent;

  private Date currentMonthsFirstDate;

  private final Label captionLabel = new Label("");

  private Button monthButton;

  private Button weekButton;

  private Button dayButton;

  private Button nextButton;

  private Button prevButton;

  private ComboBox timeZoneSelect;

  private ComboBox formatSelect;

  private ComboBox localeSelect;

  private CheckBox hideWeekendsButton;

  private CheckBox readOnlyButton;

  private TextField captionField;

  private Window scheduleEventPopup;

  private final FormLayout scheduleEventFieldLayout = new FormLayout();
  private FieldGroup scheduleEventFieldGroup = new FieldGroup();

  private Button deleteEventButton;

  private Button applyEventButton;

  private Mode viewMode = Mode.WEEK;

  private Button addNewEvent;

  private String calendarHeight = null;

  private String calendarWidth = null;

  private CheckBox disabledButton;

  private Integer firstHour;

  private Integer lastHour;

  private Integer firstDay;

  private Integer lastDay;

  private boolean showWeeklyView;

  private boolean useSecondResolution;

  private DateField startDateField;
  private DateField endDateField;

  public CalendarView() {
    setSizeFull();
    setHeight("1000px");
    setMargin(true);
    setSpacing(true);
  }

  @PostConstruct
  public void init() {
    setLocale(Locale.getDefault());

    // Initialize locale, timezone and timeformat selects.
    localeSelect = createLocaleSelect();
    timeZoneSelect = createTimeZoneSelect();
    formatSelect = createCalendarFormatSelect();

    initCalendar();
    initLayoutContent();
  }

  private void initLayoutContent() {
    initNavigationButtons();
    initHideWeekEndButton();
    initReadOnlyButton();
    initDisabledButton();
    initAddNewEventButton();

    HorizontalLayout hl = new HorizontalLayout();
    hl.setWidth("100%");
    hl.setSpacing(true);
    hl.addComponent(prevButton);
    hl.addComponent(captionLabel);

    CssLayout group = new CssLayout();
    group.addStyleName("v-component-group");
    group.addComponent(dayButton);
    group.addComponent(weekButton);
    group.addComponent(monthButton);
    hl.addComponent(group);

    hl.addComponent(nextButton);
    hl.setComponentAlignment(prevButton, Alignment.MIDDLE_LEFT);
    hl.setComponentAlignment(captionLabel, Alignment.MIDDLE_CENTER);
    hl.setComponentAlignment(group, Alignment.MIDDLE_CENTER);
    hl.setComponentAlignment(nextButton, Alignment.MIDDLE_RIGHT);

    // monthButton.setVisible(viewMode == Mode.WEEK);
    // weekButton.setVisible(viewMode == Mode.DAY);

    HorizontalLayout controlPanel = new HorizontalLayout();
    controlPanel.setSpacing(true);
    controlPanel.setWidth("100%");
    controlPanel.setMargin(new MarginInfo(false, false, true, false));
    controlPanel.addComponent(localeSelect);
    controlPanel.addComponent(timeZoneSelect);
    controlPanel.addComponent(formatSelect);
    controlPanel.addComponent(hideWeekendsButton);
    controlPanel.addComponent(readOnlyButton);
    controlPanel.addComponent(disabledButton);
    controlPanel.addComponent(addNewEvent);
    controlPanel.setExpandRatio(addNewEvent, 1.0f);

    controlPanel.setComponentAlignment(timeZoneSelect, Alignment.MIDDLE_LEFT);
    controlPanel.setComponentAlignment(formatSelect, Alignment.MIDDLE_LEFT);
    controlPanel.setComponentAlignment(localeSelect, Alignment.MIDDLE_LEFT);
    controlPanel.setComponentAlignment(hideWeekendsButton, Alignment.BOTTOM_LEFT);
    controlPanel.setComponentAlignment(readOnlyButton, Alignment.BOTTOM_LEFT);
    controlPanel.setComponentAlignment(disabledButton, Alignment.BOTTOM_LEFT);
    controlPanel.setComponentAlignment(addNewEvent, Alignment.BOTTOM_RIGHT);

    Label viewCaption = new Label("Calendar");
    viewCaption.addStyleName(ValoTheme.LABEL_H1);
    viewCaption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
    addComponent(viewCaption);
    addComponent(controlPanel);
    addComponent(hl);
    addComponent(calendarComponent);
    setRowExpandRatio(getRows() - 1, 1.0f);
  }

  private void initNavigationButtons() {
    monthButton = new Button("Month", new Button.ClickListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void buttonClick(Button.ClickEvent event) {
        switchToMonthView();
      }
    });

    weekButton = new Button("Week", new Button.ClickListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void buttonClick(Button.ClickEvent event) {
        // simulate week click
        CalendarComponentEvents.WeekClickHandler handler = (CalendarComponentEvents.WeekClickHandler)
                calendarComponent.getHandler(CalendarComponentEvents.WeekClick.EVENT_ID);
        handler.weekClick(new CalendarComponentEvents.WeekClick(calendarComponent,
                calendar.get(GregorianCalendar.WEEK_OF_YEAR),
                calendar.get(GregorianCalendar.YEAR)));
      }
    });

    dayButton = new Button("Day", new Button.ClickListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void buttonClick(Button.ClickEvent event) {
        // simulate day click
        BasicDateClickHandler handler = (BasicDateClickHandler)
                calendarComponent.getHandler(CalendarComponentEvents.DateClickEvent.EVENT_ID);
        handler.dateClick(new CalendarComponentEvents.DateClickEvent(calendarComponent, calendar.getTime()));
      }
    });

    nextButton = new Button("Next", new Button.ClickListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void buttonClick(Button.ClickEvent event) {
        handleNextButtonClick();
      }
    });

    prevButton = new Button("Prev", new Button.ClickListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void buttonClick(Button.ClickEvent event) {
        handlePreviousButtonClick();
      }
    });
  }

  private void initHideWeekEndButton() {
    hideWeekendsButton = new CheckBox("Hide weekends");
    hideWeekendsButton.addStyleName("small");
    hideWeekendsButton.setImmediate(true);
    hideWeekendsButton.addValueChangeListener(new Property.ValueChangeListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        setWeekendsHidden(hideWeekendsButton.getValue());
      }
    });
  }

  private void setWeekendsHidden(boolean weekendsHidden) {
    if (weekendsHidden) {
      int firstToShow = (GregorianCalendar.MONDAY - calendar.getFirstDayOfWeek()) % 7;
      calendarComponent.setFirstVisibleDayOfWeek(firstToShow + 1);
      calendarComponent.setLastVisibleDayOfWeek(firstToShow + 5);
    } else {
      calendarComponent.setFirstVisibleDayOfWeek(1);
      calendarComponent.setLastVisibleDayOfWeek(7);
    }
  }

  private void initReadOnlyButton() {
    readOnlyButton = new CheckBox("Read-only mode");
    readOnlyButton.addStyleName("small");
    readOnlyButton.setImmediate(true);
    readOnlyButton.addValueChangeListener(new Property.ValueChangeListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        calendarComponent.setReadOnly(readOnlyButton.getValue());
      }
    });
  }

  private void initDisabledButton() {
    disabledButton = new CheckBox("Disabled");
    disabledButton.addStyleName("small");
    disabledButton.setImmediate(true);
    disabledButton.addValueChangeListener(new Property.ValueChangeListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        calendarComponent.setEnabled(!disabledButton.getValue());
      }
    });
  }

  public void initAddNewEventButton() {
    addNewEvent = new Button("Add new event");
    addNewEvent.addStyleName("primary");
    addNewEvent.addStyleName("small");
    addNewEvent.addClickListener(new Button.ClickListener() {
      private static final long serialVersionUID = -8307244759142541067L;

      @Override
      public void buttonClick(Button.ClickEvent event) {
        Date start = new Date();
        start.setHours(0);
        start.setMinutes(0);
        start.setSeconds(0);
        Date end = getEndOfDay(calendar, start);
        showEventPopup(createNewEvent(start, end), true);
      }
    });
  }

  private void initFormFields(Layout formLayout, Class<? extends CalendarEvent> eventClass) {

    startDateField = createDateField("Start date");
    endDateField = createDateField("End date");

    final CheckBox allDayField = createCheckBox("All-day");
    allDayField.addValueChangeListener(new Property.ValueChangeListener() {
      private static final long serialVersionUID = -7104996493482558021L;

      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        Object value = event.getProperty().getValue();
        if (value instanceof Boolean && Boolean.TRUE.equals(value)) {
          setFormDateResolution(Resolution.DAY);
        } else {
          setFormDateResolution(Resolution.MINUTE);
        }
      }

    });

    captionField = createTextField("Caption");
    captionField.setInputPrompt("Event name");
    captionField.setRequired(true);
    final TextField whereField = createTextField("Where");
    whereField.setInputPrompt("Address or location");
    final TextArea descriptionField = createTextArea("Description");
    descriptionField.setInputPrompt("Describe the event");
    descriptionField.setRows(3);

    final ComboBox styleNameField = createStyleNameComboBox();
    styleNameField.setInputPrompt("Choose calendar");
    styleNameField.setTextInputAllowed(false);

    formLayout.addComponent(startDateField);
    formLayout.addComponent(endDateField);
    formLayout.addComponent(allDayField);
    formLayout.addComponent(captionField);
    formLayout.addComponent(descriptionField);
    formLayout.addComponent(styleNameField);

    scheduleEventFieldGroup.bind(startDateField, "start");
    scheduleEventFieldGroup.bind(endDateField, "end");
    scheduleEventFieldGroup.bind(captionField, "caption");
    scheduleEventFieldGroup.bind(descriptionField, "description");
    scheduleEventFieldGroup.bind(styleNameField, "styleName");
    scheduleEventFieldGroup.bind(allDayField, "allDay");
  }

  private CheckBox createCheckBox(String caption) {
    CheckBox cb = new CheckBox(caption);
    cb.setImmediate(true);
    return cb;
  }

  private TextField createTextField(String caption) {
    TextField f = new TextField(caption);
    f.setNullRepresentation("");
    return f;
  }

  private TextArea createTextArea(String caption) {
    TextArea f = new TextArea(caption);
    f.setNullRepresentation("");
    return f;
  }

  private DateField createDateField(String caption) {
    DateField f = new DateField(caption);
    if (useSecondResolution) {
      f.setResolution(Resolution.SECOND);
    } else {
      f.setResolution(Resolution.MINUTE);
    }
    return f;
  }

  private ComboBox createStyleNameComboBox() {
    ComboBox s = new ComboBox("Calendar");
    s.addContainerProperty("c", String.class, "");
    s.setItemCaptionPropertyId("c");
    Item i = s.addItem("color1");
    i.getItemProperty("c").setValue("Work");
    i = s.addItem("color2");
    i.getItemProperty("c").setValue("Personal");
    i = s.addItem("color3");
    i.getItemProperty("c").setValue("Family");
    i = s.addItem("color4");
    i.getItemProperty("c").setValue("Hobbies");
    return s;
  }

  private void initCalendar() {
    calendarComponent = new Calendar(eventProvider);
    calendarComponent.setLocale(getLocale());
    calendarComponent.setImmediate(true);

    if (calendarWidth != null || calendarHeight != null) {
      if (calendarHeight != null) {
        calendarComponent.setHeight(calendarHeight);
      }
      if (calendarWidth != null) {
        calendarComponent.setWidth(calendarWidth);
      }
    } else {
      calendarComponent.setSizeFull();
    }

    if (firstHour != null && lastHour != null) {
      calendarComponent.setFirstVisibleHourOfDay(firstHour);
      calendarComponent.setLastVisibleHourOfDay(lastHour);
    }

    if (firstDay != null && lastDay != null) {
      calendarComponent.setFirstVisibleDayOfWeek(firstDay);
      calendarComponent.setLastVisibleDayOfWeek(lastDay);
    }

    Date today = new Date();
    calendar = new GregorianCalendar(getLocale());
    calendar.setTime(today);
    calendarComponent.getInternalCalendar().setTime(today);

    // Calendar getStartDate (and getEndDate) has some strange logic which
    // returns Monday of the current internal time if no start date has been
    // set
    calendarComponent.setStartDate(calendarComponent.getStartDate());
    calendarComponent.setEndDate(calendarComponent.getEndDate());
    int rollAmount = calendar.get(GregorianCalendar.DAY_OF_MONTH) - 1;
    calendar.add(GregorianCalendar.DAY_OF_MONTH, -rollAmount);
    currentMonthsFirstDate = calendar.getTime();

    updateCaptionLabel();

    if (!showWeeklyView) {
      // resetTime(false);
      // currentMonthsFirstDate = calendar.getTime();
      // calendarComponent.setStartDate(currentMonthsFirstDate);
      // calendar.add(GregorianCalendar.MONTH, 1);
      // calendar.add(GregorianCalendar.DATE, -1);
      // calendarComponent.setEndDate(calendar.getTime());
    }

    addCalendarEventListeners();
  }

  @SuppressWarnings("serial")
  private void addCalendarEventListeners() {
    // Register week clicks by changing the schedules start and end dates.
    calendarComponent.setHandler(new BasicWeekClickHandler() {

      @Override
      public void weekClick(CalendarComponentEvents.WeekClick event) {
        // let BasicWeekClickHandler handle calendar dates, and update
        // only the other parts of UI here
        super.weekClick(event);
        updateCaptionLabel();
        switchToWeekView();
      }
    });

    calendarComponent.setHandler((CalendarComponentEvents.EventClickHandler) event -> {
      showEventPopup(event.getCalendarEvent(), false);
    });

    calendarComponent.setHandler(new BasicDateClickHandler() {

      @Override
      public void dateClick(CalendarComponentEvents.DateClickEvent event) {
        // let BasicDateClickHandler handle calendar dates, and update
        // only the other parts of UI here
        super.dateClick(event);
        switchToDayView();
      }
    });

    calendarComponent.setHandler((CalendarComponentEvents.RangeSelectHandler) this::handleRangeSelect);
  }

  private ComboBox createTimeZoneSelect() {
    ComboBox s = new ComboBox("Timezone");
    s.addStyleName("tiny");
    s.setWidth("10em");
    s.addContainerProperty("caption", String.class, "");
    s.setItemCaptionPropertyId("caption");
    s.setFilteringMode(FilteringMode.CONTAINS);

    Item i = s.addItem(DEFAULT_ITEMID);
    i.getItemProperty("caption").setValue("Default (" + TimeZone.getDefault().getID() + ")");
    for (String id : TimeZone.getAvailableIDs()) {
      if (!s.containsId(id)) {
        i = s.addItem(id);
        i.getItemProperty("caption").setValue(id);
      }
    }
    s.select("DEFAULT");
    s.setImmediate(true);
    s.addValueChangeListener(new Property.ValueChangeListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        updateCalendarTimeZone(event.getProperty().getValue());
      }
    });

    return s;
  }

  private ComboBox createCalendarFormatSelect() {
    ComboBox s = new ComboBox("Calendar format");
    s.addStyleName("tiny");
    s.setWidth("10em");
    s.addContainerProperty("caption", String.class, "");
    s.setItemCaptionPropertyId("caption");

    Item i = s.addItem(DEFAULT_ITEMID);
    i.getItemProperty("caption").setValue("Default by locale");
    i = s.addItem(Calendar.TimeFormat.Format12H);
    i.getItemProperty("caption").setValue("12H");
    i = s.addItem(Calendar.TimeFormat.Format24H);
    i.getItemProperty("caption").setValue("24H");

    s.select(DEFAULT_ITEMID);
    s.setImmediate(true);
    s.addValueChangeListener(new Property.ValueChangeListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        updateCalendarFormat(event.getProperty().getValue());
      }
    });

    return s;
  }

  private ComboBox createLocaleSelect() {
    ComboBox s = new ComboBox("Locale");
    s.addStyleName("tiny");
    s.setWidth("10em");
    s.addContainerProperty("caption", String.class, "");
    s.setItemCaptionPropertyId("caption");
    s.setFilteringMode(FilteringMode.CONTAINS);

    for (Locale l : Locale.getAvailableLocales()) {
      if (!s.containsId(l)) {
        Item i = s.addItem(l);
        i.getItemProperty("caption").setValue(getLocaleItemCaption(l));
      }
    }

    s.select(getLocale());
    s.setImmediate(true);
    s.addValueChangeListener(new Property.ValueChangeListener() {

      private static final long serialVersionUID = 1L;

      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        updateCalendarLocale((Locale) event.getProperty().getValue());
      }
    });

    return s;
  }

  private void updateCalendarTimeZone(Object timezoneId) {
    TimeZone tz = null;
    if (!DEFAULT_ITEMID.equals(timezoneId)) {
      tz = TimeZone.getTimeZone((String) timezoneId);
    }

    // remember the week that was showing, so we can re-set it later
    Date startDate = calendarComponent.getStartDate();
    calendar.setTime(startDate);
    int weekNumber = calendar.get(java.util.Calendar.WEEK_OF_YEAR);
    calendarComponent.setTimeZone(tz);
    calendar.setTimeZone(calendarComponent.getTimeZone());

    if (viewMode == Mode.WEEK) {
      calendar.set(java.util.Calendar.WEEK_OF_YEAR, weekNumber);
      calendar.set(java.util.Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
      calendarComponent.setStartDate(calendar.getTime());
      calendar.add(java.util.Calendar.DATE, 6);
      calendarComponent.setEndDate(calendar.getTime());
    }
  }

  private void updateCalendarFormat(Object format) {
    Calendar.TimeFormat calFormat = null;
    if (format instanceof Calendar.TimeFormat) {
      calFormat = (Calendar.TimeFormat) format;
    }
    calendarComponent.setTimeFormat(calFormat);
  }

  private String getLocaleItemCaption(Locale l) {
    String country = l.getDisplayCountry(getLocale());
    String language = l.getDisplayLanguage(getLocale());
    StringBuilder caption = new StringBuilder(country);
    if (caption.length() != 0) {
      caption.append(", ");
    }
    caption.append(language);
    return caption.toString();
  }

  private void updateCalendarLocale(Locale l) {
    int oldFirstDayOfWeek = calendar.getFirstDayOfWeek();
    setLocale(l);
    calendarComponent.setLocale(l);
    calendar = new GregorianCalendar(l);
    int newFirstDayOfWeek = calendar.getFirstDayOfWeek();

    // we are showing 1 week, and the first day of the week has changed
    // update start and end dates so that the same week is showing
    if (viewMode == Mode.WEEK && oldFirstDayOfWeek != newFirstDayOfWeek) {
      calendar.setTime(calendarComponent.getStartDate());
      calendar.add(java.util.Calendar.DAY_OF_WEEK, 2);
      // starting at the beginning of the week
      calendar.set(GregorianCalendar.DAY_OF_WEEK, newFirstDayOfWeek);
      Date start = calendar.getTime();

      // ending at the end of the week
      calendar.add(GregorianCalendar.DATE, 6);
      Date end = calendar.getTime();

      calendarComponent.setStartDate(start);
      calendarComponent.setEndDate(end);

      // Week days depend on locale so this must be refreshed
      setWeekendsHidden(hideWeekendsButton.getValue());
    }

  }

  private void handleNextButtonClick() {
    switch (viewMode) {
      case MONTH:
        nextMonth();
        break;
      case WEEK:
        nextWeek();
        break;
      case DAY:
        nextDay();
        break;
    }
  }

  private void handlePreviousButtonClick() {
    switch (viewMode) {
      case MONTH:
        previousMonth();
        break;
      case WEEK:
        previousWeek();
        break;
      case DAY:
        previousDay();
        break;
    }
  }

  private void handleRangeSelect(CalendarComponentEvents.RangeSelectEvent event) {
    Date start = event.getStart();
    Date end = event.getEnd();
    /*
     * If a range of dates is selected in monthly mode, we want it to end at
     * the end of the last day.
     */
    if (event.isMonthlyMode()) {
      end = getEndOfDay(calendar, end);
    }

    showEventPopup(createNewEvent(start, end), true);
  }

  private void showEventPopup(CalendarEvent event, boolean newEvent) {
    if (event == null) {
      return;
    }

    updateCalendarEventPopup(newEvent);
    updateCalendarEventForm(event);
    // TODO this only works the first time
    captionField.focus();

    if (!getUI().getWindows().contains(scheduleEventPopup)) {
      getUI().addWindow(scheduleEventPopup);
    }

  }

  /* Initializes a modal window to edit schedule event. */
  private void createCalendarEventPopup() {
    VerticalLayout layout = new VerticalLayout();
    // layout.setMargin(true);
    layout.setSpacing(true);

    scheduleEventPopup = new Window(null, layout);
    scheduleEventPopup.setWidth("300px");
    scheduleEventPopup.setModal(true);
    scheduleEventPopup.center();

    scheduleEventFieldLayout.addStyleName("light");
    scheduleEventFieldLayout.setMargin(false);
    layout.addComponent(scheduleEventFieldLayout);

    applyEventButton = new Button("Apply", new Button.ClickListener() {

      private static final long serialVersionUID = 1L;

      @Override
      public void buttonClick(Button.ClickEvent event) {
        try {
          commitCalendarEvent();
        } catch (FieldGroup.CommitException e) {
          e.printStackTrace();
        }
      }
    });
    applyEventButton.addStyleName("primary");
    Button cancel = new Button("Cancel", new Button.ClickListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void buttonClick(Button.ClickEvent event) {
        discardCalendarEvent();
      }
    });
    deleteEventButton = new Button("Delete", new Button.ClickListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void buttonClick(Button.ClickEvent event) {
        deleteCalendarEvent();
      }
    });
    deleteEventButton.addStyleName("borderless");
    scheduleEventPopup.addCloseListener(new Window.CloseListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void windowClose(Window.CloseEvent e) {
        discardCalendarEvent();
      }
    });

    HorizontalLayout buttons = new HorizontalLayout();
    buttons.addStyleName("v-window-bottom-toolbar");
    buttons.setWidth("100%");
    buttons.setSpacing(true);
    buttons.addComponent(deleteEventButton);
    buttons.addComponent(applyEventButton);
    buttons.setExpandRatio(applyEventButton, 1);
    buttons.setComponentAlignment(applyEventButton, Alignment.TOP_RIGHT);
    buttons.addComponent(cancel);
    layout.addComponent(buttons);
  }

  private void updateCalendarEventPopup(boolean newEvent) {
    if (scheduleEventPopup == null) {
      createCalendarEventPopup();
    }

    if (newEvent) {
      scheduleEventPopup.setCaption("New event");
    } else {
      scheduleEventPopup.setCaption("Edit event");
    }

    deleteEventButton.setVisible(!newEvent);
    deleteEventButton.setEnabled(!calendarComponent.isReadOnly());
    applyEventButton.setEnabled(!calendarComponent.isReadOnly());
  }

  private void updateCalendarEventForm(CalendarEvent event) {
    BeanItem<CalendarEvent> item = new BeanItem<>(event);
    scheduleEventFieldLayout.removeAllComponents();
    scheduleEventFieldGroup = new FieldGroup();
    initFormFields(scheduleEventFieldLayout, event.getClass());
    scheduleEventFieldGroup.setBuffered(true);
    scheduleEventFieldGroup.setItemDataSource(item);
  }

  private void setFormDateResolution(Resolution resolution) {
    if (startDateField != null && endDateField != null) {
      startDateField.setResolution(resolution);
      endDateField.setResolution(resolution);
    }
  }

  private CalendarEvent createNewEvent(Date startDate, Date endDate) {
    net.gtidev.test.model.Event event = new net.gtidev.test.model.Event();
    event.setCaption("");
    event.setStart(startDate);
    event.setEnd(endDate);
    event.setStyleName("color1");
    return event;
  }

  /* Removes the event from the data source and fires change event. */
  private void deleteCalendarEvent() {
    net.gtidev.test.model.Event event = getFormCalendarEvent();
    if (eventProvider.containsEvent(event)) {
      eventProvider.removeEvent(event);
    }
    getUI().removeWindow(scheduleEventPopup);
  }

  /* Adds/updates the event in the data source and fires change event. */
  private void commitCalendarEvent() throws FieldGroup.CommitException {
    scheduleEventFieldGroup.commit();
    net.gtidev.test.model.Event event = getFormCalendarEvent();
    if (event.getEnd() == null) {
      event.setEnd(event.getStart());
    }
    if (!eventProvider.containsEvent(event)) {
      eventProvider.addEvent(event);
    }

    getUI().removeWindow(scheduleEventPopup);
  }

  private void discardCalendarEvent() {
    scheduleEventFieldGroup.discard();
    getUI().removeWindow(scheduleEventPopup);
  }

  @SuppressWarnings("unchecked")
  private net.gtidev.test.model.Event getFormCalendarEvent() {
    BeanItem<net.gtidev.test.model.Event> item = (BeanItem<net.gtidev.test.model.Event>) scheduleEventFieldGroup.getItemDataSource();
    net.gtidev.test.model.Event event = item.getBean();
    return event;
  }

  private void nextMonth() {
    rollMonth(1);
  }

  private void previousMonth() {
    rollMonth(-1);
  }

  private void nextWeek() {
    rollWeek(1);
  }

  private void previousWeek() {
    rollWeek(-1);
  }

  private void nextDay() {
    rollDate(1);
  }

  private void previousDay() {
    rollDate(-1);
  }

  private void rollMonth(int direction) {
    calendar.setTime(currentMonthsFirstDate);
    calendar.add(GregorianCalendar.MONTH, direction);
    resetTime(false);
    currentMonthsFirstDate = calendar.getTime();
    calendarComponent.setStartDate(currentMonthsFirstDate);
    updateCaptionLabel();
    calendar.add(GregorianCalendar.MONTH, 1);
    calendar.add(GregorianCalendar.DATE, -1);
    resetCalendarTime(true);
  }

  private void rollWeek(int direction) {
    calendar.add(GregorianCalendar.WEEK_OF_YEAR, direction);
    calendar.set(GregorianCalendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
    resetCalendarTime(false);
    resetTime(true);
    calendar.add(GregorianCalendar.DATE, 6);
    calendarComponent.setEndDate(calendar.getTime());
  }

  private void rollDate(int direction) {
    calendar.add(GregorianCalendar.DATE, direction);
    resetCalendarTime(false);
    resetCalendarTime(true);
  }

  private void updateCaptionLabel() {
    DateFormatSymbols s = new DateFormatSymbols(getLocale());
    String month = s.getShortMonths()[calendar.get(GregorianCalendar.MONTH)];
    captionLabel.setValue(month + " " + calendar.get(GregorianCalendar.YEAR));
  }

  /*
   * Switch the view to week view.
   */
  public void switchToWeekView() {
    viewMode = Mode.WEEK;
    // weekButton.setVisible(false);
    // monthButton.setVisible(true);
  }

  /*
   * Switch the Calendar component's start and end date range to the target
   * month only. (sample range: 01.01.2010 00:00.000 - 31.01.2010 23:59.999)
   */
  public void switchToMonthView() {
    viewMode = Mode.MONTH;
    // monthButton.setVisible(false);
    // weekButton.setVisible(false);

    int rollAmount = calendar.get(GregorianCalendar.DAY_OF_MONTH) - 1;
    calendar.add(GregorianCalendar.DAY_OF_MONTH, -rollAmount);

    calendarComponent.setStartDate(calendar.getTime());

    updateCaptionLabel();

    calendar.add(GregorianCalendar.MONTH, 1);
    calendar.add(GregorianCalendar.DATE, -1);

    calendarComponent.setEndDate(calendar.getTime());

    calendar.setTime(new Date());
    // resetCalendarTime(true);
  }

  /*
   * Switch to day view (week view with a single day visible).
   */
  public void switchToDayView() {
    viewMode = Mode.DAY;
    // monthButton.setVisible(true);
    // weekButton.setVisible(true);
  }

  private void resetCalendarTime(boolean resetEndTime) {
    resetTime(resetEndTime);
    if (resetEndTime) {
      calendarComponent.setEndDate(calendar.getTime());
    } else {
      calendarComponent.setStartDate(calendar.getTime());
      updateCaptionLabel();
    }
  }

  /*
   * Resets the calendar time (hour, minute second and millisecond) either to
   * zero or maximum value.
   */
  private void resetTime(boolean max) {
    if (max) {
      calendar.set(GregorianCalendar.HOUR_OF_DAY, calendar.getMaximum(GregorianCalendar.HOUR_OF_DAY));
      calendar.set(GregorianCalendar.MINUTE, calendar.getMaximum(GregorianCalendar.MINUTE));
      calendar.set(GregorianCalendar.SECOND, calendar.getMaximum(GregorianCalendar.SECOND));
      calendar.set(GregorianCalendar.MILLISECOND, calendar.getMaximum(GregorianCalendar.MILLISECOND));
    } else {
      calendar.set(GregorianCalendar.HOUR_OF_DAY, 0);
      calendar.set(GregorianCalendar.MINUTE, 0);
      calendar.set(GregorianCalendar.SECOND, 0);
      calendar.set(GregorianCalendar.MILLISECOND, 0);
    }
  }

  private static Date getEndOfDay(java.util.Calendar calendar, Date date) {
    java.util.Calendar calendarClone = (java.util.Calendar) calendar.clone();

    calendarClone.setTime(date);
    calendarClone.set(java.util.Calendar.MILLISECOND, calendarClone.getActualMaximum(java.util.Calendar.MILLISECOND));
    calendarClone.set(java.util.Calendar.SECOND, calendarClone.getActualMaximum(java.util.Calendar.SECOND));
    calendarClone.set(java.util.Calendar.MINUTE, calendarClone.getActualMaximum(java.util.Calendar.MINUTE));
    calendarClone.set(java.util.Calendar.HOUR, calendarClone.getActualMaximum(java.util.Calendar.HOUR));
    calendarClone.set(java.util.Calendar.HOUR_OF_DAY, calendarClone.getActualMaximum(java.util.Calendar.HOUR_OF_DAY));

    return calendarClone.getTime();
  }

  private static Date getStartOfDay(java.util.Calendar calendar, Date date) {
    java.util.Calendar calendarClone = (java.util.Calendar) calendar.clone();

    calendarClone.setTime(date);
    calendarClone.set(java.util.Calendar.MILLISECOND, 0);
    calendarClone.set(java.util.Calendar.SECOND, 0);
    calendarClone.set(java.util.Calendar.MINUTE, 0);
    calendarClone.set(java.util.Calendar.HOUR, 0);
    calendarClone.set(java.util.Calendar.HOUR_OF_DAY, 0);

    return calendarClone.getTime();
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent event) {

  }
}
