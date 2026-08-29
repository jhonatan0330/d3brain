package d3.accounting.infrastructure;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;

@D3SqlConnMapper(value = "CreateCatalogoTablesAccountingMapper")
public interface CreateCatalogTablesMapper {

	void createTemporal(@Param("code") String code);

	void createPuntual(@Param("code") String code);

	void createVoucher(@Param("code") String code);

	void createRegister(@Param("code") String code);

	void createAuxiliar(@Param("code") String code);

}