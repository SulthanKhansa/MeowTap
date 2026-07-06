package services;

import dao.AdminDAO;
import model.Admin;

public class AuthService {

    private final AdminDAO adminDAO;

    public AuthService() {
        this.adminDAO = new AdminDAO();
    }

    public Admin login(String username, String password) {
        return adminDAO.checkLogin(username, password);
    }

    public boolean register(String fullname, String username, String password) {
        Admin admin = new Admin(username, util.SecurityUtils.hashSHA256(password), fullname);
        return adminDAO.register(admin);
    }
}
