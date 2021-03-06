package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import entidades.Tbl_empresa;
import entidades.Vw_empresa;

public class Dt_empresa {

	poolConexion pc = poolConexion.getInstance();
	Connection c = null;
	private ResultSet rsEmpresa = null;
	private ResultSet rs = null;
	private PreparedStatement ps = null;

	public Dt_empresa() {

	}

	// Metodo para llenar el ResultSet
	public void llenar_rsEmpresa(Connection c) {
		try {
			ps = c.prepareStatement("Select * from tbl_empresa;", ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE, ResultSet.HOLD_CURSORS_OVER_COMMIT);
			rsEmpresa = ps.executeQuery();
		} catch (Exception e) {
			System.out.println("DATOS: ERROR EN LISTAR tbl_empresa " + e.getMessage());
			e.printStackTrace();
		}
	}

	// Metodo para listar la empresa
	// Sujeto a cambio el Tbl_empresa por algun View
	public ArrayList<Vw_empresa> listarEmpresa() {

		ArrayList<Vw_empresa> listEmpresa = new ArrayList<Vw_empresa>();
		try {
			c = poolConexion.getConnection();
			ps = c.prepareStatement("SELECT * FROM vw_empresa; ", ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();
			while (rs.next()) {
				Vw_empresa tblEmpresa = new Vw_empresa();
				tblEmpresa.setIdEmpresa(rs.getInt("idEmpresa"));
				tblEmpresa.setRuc(rs.getString("ruc"));
				tblEmpresa.setRazonSocial(rs.getString("razonSocial"));
				tblEmpresa.setNombreComercial(rs.getString("nombreComercial"));
				tblEmpresa.setTelefono(rs.getString("telefono"));
				tblEmpresa.setCorreo(rs.getString("correo"));
				tblEmpresa.setDireccion(rs.getString("direccion"));
				tblEmpresa.setRepresentanteLegalNombre(rs.getString("Nombre Completo"));
				tblEmpresa.setMonedaNombre(rs.getString("nombre"));
				tblEmpresa.setDepartamentoNombre(rs.getString("departamento"));
				tblEmpresa.setMunicipioNombre(rs.getString("municipio"));
				listEmpresa.add(tblEmpresa);
			}
		} catch (Exception e) {
			System.out.println("DATOS: ERROR EN LISTAR tbl_empresa " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (c != null) {
					poolConexion.closeConnection(c);
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return listEmpresa;
	}

	// Metodo para agregar una empresa
	public boolean addEmpresa(Tbl_empresa empresa) {
		boolean guardado = false;

		try {
			c = poolConexion.getConnection();
			this.llenar_rsEmpresa(c);
			this.rsEmpresa.moveToInsertRow();

			rsEmpresa.updateInt("idEmpresa", empresa.getIdEmpresa());
			rsEmpresa.updateString("ruc", empresa.getRuc());
			rsEmpresa.updateString("razonSocial", empresa.getRazonSocial());
			rsEmpresa.updateString("nombreComercial", empresa.getNombreComercial());
			rsEmpresa.updateString("telefono", empresa.getTelefono());
			rsEmpresa.updateString("correo", empresa.getCorreo());
			rsEmpresa.updateString("direccion", empresa.getDireccion());
			rsEmpresa.updateInt("tbl_representanteLegal_idRepresentanteLegal", empresa.getIdRepresentanteLegal());
			rsEmpresa.updateInt("tbl_moneda_idMoneda", empresa.getIdMoneda());
			rsEmpresa.updateInt("tbl_departamento_idDepartamento", empresa.getIdDepartamento());
			rsEmpresa.updateInt("tbl_municipio_idMunicipio", empresa.getIdMunicipio());
			rsEmpresa.updateDate("fechaCreacion", empresa.getFechaCreacion());
			rsEmpresa.updateInt("usuarioCreacion", empresa.getUsuarioCreacion());

			rsEmpresa.insertRow();
			rsEmpresa.moveToCurrentRow();
			guardado = true;
		} catch (Exception e) {
			System.err.println("ERROR AL GUARDAR EMPRESA: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (rsEmpresa != null) {
					rsEmpresa.close();
				}
				if (c != null) {
					poolConexion.closeConnection(c);
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return guardado;
	}

}