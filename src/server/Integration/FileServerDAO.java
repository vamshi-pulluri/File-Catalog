package server.Integration;


import server.Databaseutil.Databaseutil;
import server.Model.FileException;
import server.Model.ProcessFile;
import server.Model.Account;
import server.Model.UserException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FileServerDAO {

	public Account findUserByName(String username) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement pst = null;
		Account u = null;
		long userid;
		if (username == null) {
			return null;
		}
		try {
			con = Databaseutil.getConnection();
			if (con != null) {
				pst = con
						.prepareStatement("select userid_seq.nextval from dual");
				ResultSet rs = pst.executeQuery();System.out.println("dragon dragon");
				while (rs.next()) {
					userid = rs.getLong(1);System.out.println("USERID"+" "+userid);
					ps = con.prepareStatement("select * from USER_FILE where username=?");
					ps.setString(1, username);
					ResultSet rt = ps.executeQuery();
					if (rt.next()) {
						u = new Account(rt.getLong(1),rt.getString(2), rt.getString(3));
					}
				}

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			Databaseutil.closeConnection(con);
			Databaseutil.closeStatement(pst);
			Databaseutil.closeStatement(ps);
		}
		return u;

	}

	public void updateFile(String filename, String publicFile, String publicWrite, String publicRead) {
		// commitTransaction();
		Connection con = null;
		PreparedStatement ps = null;
		int x = 0;
		try {
			con = Databaseutil.getConnection();
			if (con != null) {
				ps = con.prepareStatement("update FILES_CATALOG set publicFile=?,publicWrite=?,publicRead=? where name=?");
				//System.out.println("entered update file user:"+ metafile.getUsername());
				ps.setString(1, publicFile);
				//System.out.println(metafile.getName()+" "+metafile.getSize()+" "+ metafile.getPublicFile()+" "+metafile.getPublicWrite()+" "+metafile.getPublicRead());
				ps.setString(2, publicWrite);
				ps.setString(3, publicRead);
				ps.setString(4, filename);
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			Databaseutil.closeConnection(con);
			Databaseutil.closeStatement(ps);
		}

	}

	public boolean register(Account newUser) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement pst = null;
		long val = 0;
		boolean flag = false;
		try {
			con = Databaseutil.getConnection();
			pst = con.prepareStatement("select userid_seq.nextval from dual");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {System.out.println("2.place");
				val = rs.getLong(1);
				newUser.setUserID(val);
				ps = con.prepareStatement("insert into USER_FILE values(?,?,?)");
				ps.setLong(1, val);
				ps.setString(2, newUser.getUsername());
				ps.setString(3, newUser.getPassword());
				ps.executeUpdate();
				flag = true;
				System.out.println("successfully registered");
			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			Databaseutil.closeConnection(con);
		}
		return flag;
	}

	public void unRegister(String username) throws UserException {
		Connection con = null;
		PreparedStatement ps = null;
		int x = 0;
		try {
			con = Databaseutil.getConnection();
			if (con != null) {
				ps = con.prepareStatement("delete from USER_FILE where username=?");
				ps.setString(1, username);
				x = ps.executeUpdate();
				System.out.println("No of rows processed" + x);
				if (x != 1) {
					throw new UserException("Could not delete user: "
							+ username);
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			Databaseutil.closeConnection(con);
			Databaseutil.closeStatement(ps);
		}
	}

	public ProcessFile findFileByName(String filename,
			boolean endTransactionAfterSearching) {
		Connection con = null;
		PreparedStatement ps = null;
		ProcessFile metafile = null;
		if (filename == null) {
			return null;
		}
		try {
			con = Databaseutil.getConnection();
			if (con != null) {
				ps = con.prepareStatement("select FILES_CATALOG.name, FILES_CATALOG.sizes, FILES_CATALOG.publicFile, FILES_CATALOG.publicWrite,FILES_CATALOG.publicRead, USER_FILE.username FROM FILES_CATALOG, USER_FILE where FILES_CATALOG.username= USER_FILE.username and FILES_CATALOG.name= ?");
				ps.setString(1, filename);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					metafile = new ProcessFile();
					metafile.setName(rs.getString(1));
					metafile.setSize(rs.getLong(2));
					metafile.setPublicFile(rs.getString(3));
					metafile.setPublicWrite(rs.getString(4));
					metafile.setPublicRead(rs.getString(5));
					metafile.getOwner().setUsername(rs.getString(6));
					System.out.println("6"+ " "+rs.getString(6));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			Databaseutil.closeConnection(con);
			Databaseutil.closeStatement(ps);
		}
		return metafile;
	}

	public boolean createFile(ProcessFile file) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean flag = false;
		try {
			con = Databaseutil.getConnection();
			if (con != null) {
				ps = con.prepareStatement("insert into FILES_CATALOG values(?,?,?,?,?,?)");
				ps.setString(1, file.getName());
				System.out.println("name of the file"+" "+file.getName());
				ps.setLong(2, file.getSize());
				ps.setString(3, file.getOwner().getUsername());
				ps.setString(4, file.getPublicFile());
				ps.setString(5, file.getPublicWrite());
				ps.setString(6, file.getPublicRead());
				ps.executeUpdate();
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			Databaseutil.closeConnection(con);
			Databaseutil.closeStatement(ps);
		}
		return flag;
	}

	public void deleteFile(ProcessFile file) throws FileException {
		Connection con = null;
		PreparedStatement ps = null;
		int x = 0;
		try {
			con = Databaseutil.getConnection();
			if (con != null) {
				ps = con.prepareStatement("delete from FILES_CATALOG where name=?");
				ps.setString(1, file.getName());
				x = ps.executeUpdate();
				if (x > 0) {
					System.out.println("No of file rows deleted" + x);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			Databaseutil.closeConnection(con);
			Databaseutil.closeStatement(ps);
		}
	}

	public ArrayList<ProcessFile> getUserFiles(long userID) {
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<ProcessFile> list = new ArrayList<>();
		con = Databaseutil.getConnection();
		ProcessFile metafile = null;
		try {
			ps = con.prepareStatement("select FILES_CATALOG.name, FILES_CATALOG.sizes, FILES_CATALOG.publicFile, FILES_CATALOG.publicWrite,FILES_CATALOG.publicRead, FILES_CATALOG.username FROM FILES_CATALOG, USER_FILE where USER_FILE.userid=?");
			ps.setLong(1, userID);
			System.out.println("user id"+ userID);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {System.out.println("whaleeeeeeeeees"+rs.getString(1)+ rs.getLong(2));
				metafile = new ProcessFile();
				metafile.setName(rs.getString(1));
				System.out.println("1"+ rs.getString(1));
				metafile.setSize(rs.getLong(2));
				System.out.println("2"+ rs.getLong(2));
				metafile.setPublicFile(rs.getString(3));
				System.out.println("4"+ rs.getString(3));
				metafile.setPublicWrite(rs.getString(4));
				System.out.println("5"+ rs.getString(4));
				metafile.setPublicRead(rs.getString(5));
				System.out.println("6"+ rs.getString(5));
				metafile.getOwner().setUsername(rs.getString(6));
				System.out.println("hererereerer"+ rs.getString(6));
				list.add(metafile);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
