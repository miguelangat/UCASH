package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import entidades.Tbl_periodoFiscal;

public class Dt_periodoFiscal {
	poolConexion pc = poolConexion.getInstance();
	Connection c = null;
	private ResultSet rsperiodoFiscal = null;
	private ResultSet rs = null;
	private PreparedStatement ps = null;
	
	public Dt_periodoFiscal() {
		
	}
	
	public void llenaRsPeriodoFiscal(Connection c) {
		try {
			this.ps = c.prepareStatement("SELECT * FROM sistemacontablebd.tbl_periodoFiscal;", ResultSet.TYPE_SCROLL_SENSITIVE,  ResultSet.CONCUR_UPDATABLE, ResultSet.HOLD_CURSORS_OVER_COMMIT);
			this.rsperiodoFiscal = this.ps.executeQuery();
			
		} catch(Exception e) {
			System.out.println("DATOS: ERROR EN LISTAR Periodo Fiscal " + e.getMessage());
			e.printStackTrace();
		}
	}
	public ArrayList<Tbl_periodoFiscal> listarperiodoFiscal(){
		ArrayList<Tbl_periodoFiscal> listperiodoFiscal = new ArrayList<Tbl_periodoFiscal>();
		try {
			c = poolConexion.getConnection();
			ps = c.prepareStatement("SELECT * FROM sistemacontablebd.tbl_periodoFiscal;",  ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();
			
			while(this.rs.next()) {
				Tbl_periodoFiscal periodofiscal = new Tbl_periodoFiscal();
				periodofiscal.setIdPeriodoFiscal(rs.getInt("idperiodoFiscal"));
				periodofiscal.setFechaInicio(rs.getDate("fechaInicio"));
				periodofiscal.setFechaFinal(rs.getDate("fechaFinal"));
				periodofiscal.setEstado(rs.getInt("estado"));
				listperiodoFiscal.add(periodofiscal);
			}
			} catch(Exception e) {
				System.out.println("DATOS: ERROR EN LISTAR Periodos Fiscales "+e.getMessage());
				e.printStackTrace();
			}
		 finally {
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
	                e.printStackTrace();
	            }

		}
		
		return listperiodoFiscal;
	}
	public boolean agregarPeriodoFiscal(Tbl_periodoFiscal tpf) {
		boolean guardado = false;
		
		try {
			c = poolConexion.getConnection();
			this.llenaRsPeriodoFiscal(c);
			this.rsperiodoFiscal.moveToInsertRow();
			rsperiodoFiscal.updateInt("idPeriodoFiscal", tpf.getIdPeriodoFiscal());
			rsperiodoFiscal.insertRow();
			rsperiodoFiscal.moveToCurrentRow();
			guardado = true;
		}
		catch(Exception e) {
			System.err.println("ERROR AL GUARDAR tbl_periodoFiscal "+e.getMessage());
			e.printStackTrace();
		}
		finally {
			try {
				if(rsperiodoFiscal != null) {
					rsperiodoFiscal.close();
				}
				if(c != null) {
					poolConexion.closeConnection(c);
				}
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
			}
		return guardado;
		}
	}
