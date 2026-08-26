package d3.accounting_plan.application;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.accounting_plan.application.base.CatalogService;
import d3.accounting_plan.application.base.ResultMapExtendService;
import d3.accounting_plan.application.base.TimeFrameService;
import d3.accounting_plan.domain.CatalogDTO;
import d3.accounting_plan.domain.CatalogFilterDTO;
import d3.accounting_plan.domain.TimeFrameDTO;
import d3.accounting_plan.domain.TimeFrameFilterDTO;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import org.springframework.context.annotation.Lazy;

@Service("PlanCreateCatalogTemplateAccountingService")
public class PlanCreateCatalogService {

	private final CatalogService catalogService;
	private final ResultMapExtendService mapService;
	private final TimeFrameService timeFrameService;

	public PlanCreateCatalogService(@Lazy CatalogService catalogService, @Lazy ResultMapExtendService mapService,
			@Lazy TimeFrameService timeFrameService) {
		this.catalogService = catalogService;
		this.mapService = mapService;
		this.timeFrameService = timeFrameService;
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public CatalogDTO call(CatalogDTO catalog) throws ServerException {
		validateCatalog(catalog);
		if (catalog.getKey() != null) {
			catalogService.update(catalog);
		} else {
			catalogService.save(catalog);
		}
		createTemporalFrame(catalog.getInitialDate(), catalog.getFinalDate());
		return catalogService.getById(catalog.getKey());
	}

	private void validateCatalog(CatalogDTO catalog) throws ServerException {
		if (catalog == null)
			throw new ServerException("No se reconoce catalogo");
		if (catalog.getCode() == null)
			throw new ServerException("El codigo del catalogo es obligatorio");
		if (catalog.getTemplate() == null)
			throw new ServerException("La plantilla del catalogo es obligatorio");
		if (!catalog.getCode().matches("[0-9A-Za-z]+"))
			throw new ServerException("El codigo solo puede tener letras y numeros y no puede tener espacios.");
		if (catalog.getCode().length() > 20)
			throw new ServerException("El codigo menos de 20 digitos");
		if (catalog.getName() == null)
			throw new ServerException("El nombre del catalogo es obligatorio");
		// if (catalog.getInitialDate() == null ) //|| catalog.getFinalDate() == null
		// throw new ServerException("La fecha de inicio del catalogo es obligatorio");

		if (catalog.getFinalDate() != null) {

			if (catalog.getInitialDate().compareTo(catalog.getFinalDate()) > 0)
				throw new ServerException("La fecha de inicio debe ser menor a la fecha de fin del catalogo");

			Calendar fecha = Calendar.getInstance();
			fecha.setTime(catalog.getFinalDate());
			int ultimoDiaDelMes = fecha.getActualMaximum(Calendar.DAY_OF_MONTH);
			if (fecha.get(Calendar.DAY_OF_MONTH) != ultimoDiaDelMes)
				throw new ServerException("La fecha de fin del catalogo debe ser el último día del mes");
		}

		CatalogFilterDTO filter = new CatalogFilterDTO();
		filter.setCode(catalog.getCode());
		CatalogDTO catalogDB = catalogService.getOne(filter);
		if (catalogDB != null && (catalog.getKey() == null || catalog.getKey().compareTo(catalogDB.getKey()) != 0))
			throw new ServerException("Ya existe un catalogo con este codigo");
	}

	public CatalogDTO callDelete(String catalogId) throws ServerException {
		return catalogService.delete(catalogId);
	}

	public void validateTemporalFrame(Date pDate) throws ServerException {
		TimeFrameFilterDTO _filter = new TimeFrameFilterDTO();
		_filter.setLevel(1);
		LocalDate localDate = pDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		_filter.setCode(String.valueOf(localDate.getYear()));
		_filter.setState(SharedConstants.STATE_ACTIVE);
		TimeFrameDTO _year = timeFrameService.getOne(_filter);
		if (_year == null) {

			createTemporalFrame(pDate, null);
		}

	}

	private void createTemporalFrame(Date initialDate, Date finalDate) throws ServerException {

		if (initialDate == null)
			throw new ServerException("La fecha de inicio del catalogo es obligatorio");

		Calendar _date = Calendar.getInstance();
		_date.setTime(initialDate);
		_date.set(Calendar.MONTH, 0);
		_date.set(Calendar.DAY_OF_MONTH, 1);
		_date.set(Calendar.HOUR, 0);
		_date.set(Calendar.MINUTE, 0);
		_date.set(Calendar.SECOND, 0);
		initialDate = _date.getTime();

		if (finalDate == null) {
			Calendar fecha = Calendar.getInstance();
			fecha.setTime(initialDate);
			fecha.add(Calendar.YEAR, 1);
			finalDate = fecha.getTime();
		}
		// Esta pensado para tener 5 niveles de detalle sino que los niveles 4 y 5 traen
		// muchos registros
		// La idea es colocar una propiedad que permita aumentar o reducir los niveles
		int maxLevel = 4;

		TimeFrameDTO timeFrame = mapService.getTimeFrameLevel(0);
		if (timeFrame.getStartDate() == null) {
			createLevel0(initialDate, finalDate);
		} else {
			// Esta parte es para actualizar el rango de fechas del nivel 0
			boolean updateFlag = false;
			if (initialDate.getTime() < timeFrame.getStartDate().getTime()) {
				timeFrame.setStartDate(initialDate);
				updateFlag = true;
			}
			Calendar date = Calendar.getInstance();
			date.setTime(timeFrame.getEndDate());
			date.add(Calendar.DATE, 1);
			if (finalDate.getTime() < date.getTime().getTime()) {
				timeFrame.setEndDate(date.getTime());
				updateFlag = true;
			}
			if (updateFlag)
				timeFrameService.update(timeFrame);
		}
		if (maxLevel >= 1)
			getDateToCreateLevel(initialDate, finalDate, 1);
		if (maxLevel >= 2)
			getDateToCreateLevel(initialDate, finalDate, 2);
		if (maxLevel >= 3)
			getDateToCreateLevel(initialDate, finalDate, 3);
		if (maxLevel >= 4)
			getDateToCreateLevel(initialDate, finalDate, 4);
		if (maxLevel >= 5)
			getDateToCreateLevel(initialDate, finalDate, 5);
	}

	private void getDateToCreateLevel(Date initialDate, Date finalDate, int level) throws ServerException {

		TimeFrameDTO timeFrame = mapService.getTimeFrameLevel(level);
		if (timeFrame.getStartDate() == null || initialDate.getTime() < timeFrame.getStartDate().getTime()) {
			if (timeFrame.getStartDate() != null) {
				if (level == 1)
					createLevel1(initialDate, timeFrame.getStartDate());
				if (level == 2)
					createLevel2(initialDate, timeFrame.getStartDate());
				if (level == 3)
					createLevel3(initialDate, timeFrame.getStartDate());
				if (level == 4)
					createLevel4(initialDate, timeFrame.getStartDate());
				if (level == 5)
					createLevel5(initialDate, timeFrame.getStartDate());
			} else {
				if (level == 1)
					createLevel1(initialDate, finalDate);
				if (level == 2)
					createLevel2(initialDate, finalDate);
				if (level == 3)
					createLevel3(initialDate, finalDate);
				if (level == 4)
					createLevel4(initialDate, finalDate);
				if (level == 5)
					createLevel5(initialDate, finalDate);
				// No necesito realizar la actualizacion de fecha final
				return;
			}
		}

		if (timeFrame.getEndDate() == null || finalDate.getTime() > timeFrame.getEndDate().getTime()) {
			if (level == 1)
				createLevel1(timeFrame.getEndDate(), finalDate);
			if (level == 2)
				createLevel2(timeFrame.getEndDate(), finalDate);
			if (level == 3)
				createLevel3(timeFrame.getEndDate(), finalDate);
			if (level == 4)
				createLevel4(timeFrame.getEndDate(), finalDate);
			if (level == 5)
				createLevel5(timeFrame.getEndDate(), finalDate);
		}
	}

	private void createLevel0(Date initialDate, Date endDate) throws ServerException {
		TimeFrameDTO map0 = new TimeFrameDTO();// getBaseMap(accountId, catalogId, type, null);
		map0.setLevel(0);
		map0.setStartDate(initialDate);
		Calendar date = Calendar.getInstance();
		date.setTime(endDate);
		date.add(Calendar.DATE, 1);
		map0.setEndDate(date.getTime());
		map0.setCode("0");
		timeFrameService.save(map0);
	}

	private void createLevel1(Date initialDate, Date endDate) throws ServerException {
		Calendar date = Calendar.getInstance();
		date.setTime(initialDate);
		date.set(Calendar.MONTH, 0);
		date.set(Calendar.DAY_OF_MONTH, 1);
		while (date.getTime().compareTo(endDate) < 0) {
			TimeFrameDTO map = new TimeFrameDTO();
			map.setLevel(1);
			map.setStartDate(date.getTime());
			map.setCode(String.valueOf(date.get(Calendar.YEAR)));
			map.setYear(date.get(Calendar.YEAR));

			date.add(Calendar.YEAR, 1);
			date.set(Calendar.MONTH, 0);
			date.set(Calendar.DAY_OF_MONTH, 1);
			map.setEndDate(date.getTime());
			timeFrameService.save(map);
		}

	}

	private void createLevel2(Date initialDate, Date endDate) throws ServerException {
		Calendar date = Calendar.getInstance();
		date.setTime(initialDate);
		date.set(Calendar.DAY_OF_MONTH, 1);
		while (date.getTime().compareTo(endDate) < 0) {
			TimeFrameDTO map = new TimeFrameDTO();
			map.setLevel(2);
			map.setStartDate(date.getTime());
			map.setYear(date.get(Calendar.YEAR));
			map.setMonth(date.get(Calendar.MONTH));
			map.setCode(String.valueOf(date.get(Calendar.YEAR)) + "-" + "%02d".formatted(date.get(Calendar.MONTH) + 1));
			date.add(Calendar.MONTH, 1);
			date.set(Calendar.DAY_OF_MONTH, 1);
			map.setEndDate(date.getTime());
			timeFrameService.save(map);
		}

	}

	private void createLevel3(Date initialDate, Date endDate) throws ServerException {
		Calendar date = Calendar.getInstance();
		date.setTime(initialDate);

		List<TimeFrameDTO> items = new ArrayList<>();

		while (date.getTime().compareTo(endDate) <= 0) {
			TimeFrameDTO map = new TimeFrameDTO();// getBaseMap(accountId, catalogId, type, mapCurrent);
			map.setLevel(3);
			map.setStartDate(date.getTime());
			map.setYear(date.get(Calendar.YEAR));
			map.setMonth(date.get(Calendar.MONTH));
			map.setDay(date.get(Calendar.DATE));
			map.setCode(String.valueOf(date.get(Calendar.YEAR)) + "-" + "%02d".formatted(date.get(Calendar.MONTH) + 1)
					+ "-" + "%02d".formatted(date.get(Calendar.DATE)));
			date.add(Calendar.DATE, 1);
			map.setEndDate(date.getTime());
			items.add(map);
		}
		mapService.saveAll(items);
	}

	private void createLevel4(Date initialDate, Date endDate) throws ServerException {
		List<TimeFrameDTO> items = new ArrayList<>();
		Calendar date = Calendar.getInstance();
		date.setTime(initialDate);
		while (date.getTime().compareTo(endDate) < 0) {
			TimeFrameDTO map = new TimeFrameDTO();// getBaseMap(accountId, catalogId, type, mapCurrent);
			map.setLevel(4);
			map.setStartDate(date.getTime());
			map.setYear(date.get(Calendar.YEAR));
			map.setMonth(date.get(Calendar.MONTH));
			map.setDay(date.get(Calendar.DATE));
			map.setHour(date.get(Calendar.HOUR_OF_DAY));
			map.setCode(String.valueOf(date.get(Calendar.YEAR)) + "-" + "%02d".formatted(date.get(Calendar.MONTH) + 1)
					+ "-" + "%02d".formatted(date.get(Calendar.DATE)) + " "
					+ "%02d".formatted(date.get(Calendar.HOUR_OF_DAY)) + ":00 - "
					+ "%02d".formatted(date.get(Calendar.HOUR_OF_DAY)) + ":59");
			date.add(Calendar.HOUR, 1);
			map.setEndDate(date.getTime());
			items.add(map);
		}
		mapService.saveAll(items);
	}

	private void createLevel5(Date initialDate, Date endDate) throws ServerException {
		List<TimeFrameDTO> items = new ArrayList<>();
		Calendar date = Calendar.getInstance();
		date.setTime(initialDate);
		while (date.getTime().compareTo(endDate) < 0) {
			TimeFrameDTO map = new TimeFrameDTO();// getBaseMap(accountId, catalogId, type, mapCurrent);
			map.setLevel(5);
			map.setStartDate(date.getTime());
			map.setYear(date.get(Calendar.YEAR));
			map.setMonth(date.get(Calendar.MONTH));
			map.setDay(date.get(Calendar.DATE));
			map.setHour(date.get(Calendar.HOUR_OF_DAY));
			map.setMinute(date.get(Calendar.MINUTE));
			map.setCode(String.valueOf(date.get(Calendar.YEAR)) + "-" + "%02d".formatted(date.get(Calendar.MONTH) + 1)
					+ "-" + "%02d".formatted(date.get(Calendar.DATE)) + " "
					+ "%02d".formatted(date.get(Calendar.HOUR_OF_DAY)) + ":"
					+ "%02d".formatted(date.get(Calendar.MINUTE)) + " - "
					+ "%02d".formatted(date.get(Calendar.HOUR_OF_DAY)) + ":"
					+ "%02d".formatted(date.get(Calendar.MINUTE) + 9));
			date.add(Calendar.MINUTE, 10);
			map.setEndDate(date.getTime());
			items.add(map);
		}
		mapService.saveAll(items);
	}
}
