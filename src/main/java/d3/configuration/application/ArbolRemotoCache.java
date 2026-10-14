package d3.configuration.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import d3.configuration.domain.HierarchyExporterDTO;
import d3.configuration.domain.TreeNodeDTO;
import d3.multitenancy.application.TenantContext;
import d3.shared.domain.ServerException;

@Service
public class ArbolRemotoCache {

	public static final long TTL_MILLIS = 120L * 60L * 1000L;
	public static final int MAX_ENTRADAS = 20;

	public static class EntradaRemota {

		private final HierarchyExporterDTO hierarchy;
		private final TreeNodeDTO arbol;
		private final String tenantOrigen;
		private final Date fechaExport;
		private final String usuario;
		private final String tenantRegistro;
		private final long creado;

		EntradaRemota(HierarchyExporterDTO hierarchy, TreeNodeDTO arbol, String tenantOrigen, Date fechaExport,
				String usuario, String tenantRegistro) {
			this.hierarchy = hierarchy;
			this.arbol = arbol;
			this.tenantOrigen = tenantOrigen;
			this.fechaExport = fechaExport;
			this.usuario = usuario;
			this.tenantRegistro = tenantRegistro;
			this.creado = System.currentTimeMillis();
		}

		public HierarchyExporterDTO getHierarchy() {
			return hierarchy;
		}

		public TreeNodeDTO getArbol() {
			return arbol;
		}

		public String getTenantOrigen() {
			return tenantOrigen;
		}

		public Date getFechaExport() {
			return fechaExport;
		}

		public String getUsuario() {
			return usuario;
		}

		public long getCreado() {
			return creado;
		}

		public boolean expirada() {
			return System.currentTimeMillis() - creado > TTL_MILLIS;
		}
	}

	private final ConcurrentHashMap<String, EntradaRemota> entradas = new ConcurrentHashMap<>();

	public String registrar(HierarchyExporterDTO hierarchy, TreeNodeDTO arbol, String tenantOrigen, Date fechaExport,
			String usuario) {
		limpiarExpiradas();
		String arbolId = UUID.randomUUID().toString();
		entradas.put(arbolId, new EntradaRemota(hierarchy, arbol, tenantOrigen, fechaExport, usuario,
				tenantActual()));
		evictarExcedentes();
		return arbolId;
	}

	public EntradaRemota obtener(String arbolId) throws ServerException {
		if (arbolId == null || arbolId.isEmpty())
			throw new ServerException("No se recibio el identificador de la configuracion remota (arbolId)");
		EntradaRemota entrada = entradas.get(arbolId);
		if (entrada == null)
			throw new ServerException(
					"La configuracion remota no esta disponible o expiro; vuelva a cargar el archivo en el paso 2");
		if (entrada.expirada()) {
			entradas.remove(arbolId);
			throw new ServerException(
					"La configuracion remota expiro; vuelva a cargar el archivo en el paso 2");
		}
		if (!tenantActual().equals(entrada.tenantRegistro)) {
			entradas.remove(arbolId);
			throw new ServerException("La configuracion remota no pertenece al entorno actual");
		}
		return entrada;
	}

	private String tenantActual() {
		String tenant = TenantContext.getCurrentTenant();
		return tenant == null ? "" : tenant;
	}

	private void limpiarExpiradas() {
		List<String> expiradas = new ArrayList<>();
		for (java.util.Map.Entry<String, EntradaRemota> entry : entradas.entrySet()) {
			if (entry.getValue().expirada())
				expiradas.add(entry.getKey());
		}
		for (String clave : expiradas)
			entradas.remove(clave);
	}

	private void evictarExcedentes() {
		while (entradas.size() > MAX_ENTRADAS) {
			String masAntigua = null;
			long momento = Long.MAX_VALUE;
			for (java.util.Map.Entry<String, EntradaRemota> entry : entradas.entrySet()) {
				if (entry.getValue().getCreado() < momento) {
					momento = entry.getValue().getCreado();
					masAntigua = entry.getKey();
				}
			}
			if (masAntigua == null)
				return;
			entradas.remove(masAntigua);
		}
	}

}
