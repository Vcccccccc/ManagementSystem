package com.example.managementsystem.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.managementsystem.Dao.*;
import com.example.managementsystem.Domain.*;
import com.example.managementsystem.Service.PersonnelManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.Date;
import java.time.LocalDate;
import java.util.*;

@Service
public class PersonnelManagementServiceImpl implements PersonnelManagementService {

    @Autowired
    private TUserDao tUserDao;

    @Autowired
    private TDeptDao tDeptDao;

    @Autowired
    private TEmployeeDao tEmployeeDao;

    @Autowired
    private THolidayDao tHolidayDao;

    @Autowired
    private TConfigDao tConfigDao;

    @Autowired
    private TRoleDao tRoleDao;

    @Autowired
    private TMenuDao tMenuDao;

    @Autowired
    private TPermissionsDao tPermissionsDao;

    @Override
    public boolean login(TUser user) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("user_account",user.getUserAccount());
        map.put("user_pwd",user.getUserPwd());

        if(tUserDao.selectByMap(map).size() == 1)
            return true;
        else
            return false;
    }

    @Override
    public List<TUser> getUsers() {
        return tUserDao.selectList(null);
    }

    @Override
    public TUser getUserByAccount(String account) {
        QueryWrapper<TUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",account);
        return tUserDao.selectOne(queryWrapper);
    }

    @Override
    public List<TDept> getAllDepts() {
        return tDeptDao.selectList(null);
    }

    @Override
    public String deleteDeptById(int id) {
        List<TEmployee> tEmployees = new ArrayList<>();
        QueryWrapper<TEmployee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("emp_dept_id",id);
        if(tEmployeeDao.selectList(queryWrapper).size() != 0)
            return "???????????????????????????????????????????????????";
        else{
            if(tDeptDao.deleteById(id) == 1)
                return "????????????";
            else
                return "????????????";
        }

    }

    @Override
    public String updateDeptById(TDept tDept) {
        QueryWrapper<TDept> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dept_id",tDept.getDeptId());
        if (!tDeptDao.selectById(tDept.getDeptId()).getDeptNo().equals(tDept.getDeptNo()))
            return "??????????????????????????????";
        if(tDeptDao.update(tDept,queryWrapper) == 1)
            return "????????????";
        else
            return "????????????";

    }

    @Override
    public List<TEmployee> getAllEmployee() {
        return tEmployeeDao.selectList(null);
    }

    @Override
    public List<TEmployee> getEmployeeByName(String name) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like("emp_name",name);
        return tEmployeeDao.selectList(queryWrapper);
    }

    @Override
    public String addEmployee(TEmployee tEmployee) {
        if(tEmployeeDao.insert(tEmployee) == 1)
            return "???????????????";
        else
            return "???????????????";
    }

    @Override
    public String delEmployeeById(int id) {
        if(tEmployeeDao.deleteById(id) == 1)
            return "????????????";
        else
            return "????????????";
    }

    @Override
    public String updateEmployee(TEmployee tEmployee) {
        QueryWrapper<TEmployee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("emp_id",tEmployee.getEmpId());
        if (!tEmployeeDao.selectById(tEmployee.getEmpId()).getEmpNo().equals(tEmployee.getEmpNo()))
            return "??????????????????????????????";
        if(tEmployeeDao.update(tEmployee,queryWrapper) == 1)
            return "????????????";
        else
            return "????????????";
    }

    @Override
    public List<LeaveForm> getAllHolidays() {
        List <THoliday> tHolidays = tHolidayDao.selectList(null);
        List <LeaveForm> leaveForms = new ArrayList<>();
        for(THoliday tHoliday:tHolidays){
            LeaveForm leaveForm = new LeaveForm();
            leaveForm.setHolidayBz(tHoliday.getHolidayBz());
            leaveForm.setHolidayEndTime(tHoliday.getHolidayEndTime());
            leaveForm.setHolidayNo(tHoliday.getHolidayNo());
            leaveForm.setHolidayType(tHolidayDao.getHolidayType(tHoliday.getHolidayTypeId()));
            leaveForm.setHolidayStatus(tHoliday.getHolidayStatus());
            leaveForm.setHolidayStartTime(tHoliday.getHolidayStartTime());
            leaveForm.setHolidayUserNo(tHoliday.getHolidayUserNo());
            leaveForms.add(leaveForm);
        }
        return leaveForms;
    }

    @Override
    public List<LeaveForm> getHolidaysByCondition(String holidayUserNo, String holidayType, String holidayStatus) {
        List<LeaveForm> leaveForms = new ArrayList<>();
        if(holidayUserNo != null){
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.like("holiday_user_no",holidayUserNo);
            List<THoliday> tHolidays = tHolidayDao.selectList(queryWrapper);
            for(THoliday tHoliday:tHolidays){
                LeaveForm leaveForm = new LeaveForm(tHoliday.getHolidayNo(),tHoliday.getHolidayUserNo(),tHolidayDao.getHolidayType(tHoliday.getHolidayId()),tHoliday.getHolidayBz(),tHoliday.getHolidayEndTime(),tHoliday.getHolidayStartTime(),tHoliday.getHolidayStatus());
                leaveForms.add(leaveForm);
            }
            return leaveForms;

        }
        else if(holidayType != null){
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("config_name",holidayType);
            int holidayTypeId = tConfigDao.selectOne(queryWrapper).getConfigId();
            queryWrapper.clear();

            queryWrapper.eq("holiday_type_id",holidayTypeId);
            List<THoliday> tHolidays = tHolidayDao.selectList(queryWrapper);
            for(THoliday tHoliday:tHolidays){
                LeaveForm leaveForm = new LeaveForm(tHoliday.getHolidayNo(),tHoliday.getHolidayUserNo(),tHolidayDao.getHolidayType(tHoliday.getHolidayId()),tHoliday.getHolidayBz(),tHoliday.getHolidayEndTime(),tHoliday.getHolidayStartTime(),tHoliday.getHolidayStatus());
                leaveForms.add(leaveForm);
            }
            return leaveForms;
        }
        else if(holidayStatus != null){
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("holiday_status",holidayStatus);
            List<THoliday> tHolidays = tHolidayDao.selectList(queryWrapper);
            for(THoliday tHoliday:tHolidays){
                LeaveForm leaveForm = new LeaveForm(tHoliday.getHolidayNo(),tHoliday.getHolidayUserNo(),tHolidayDao.getHolidayType(tHoliday.getHolidayId()),tHoliday.getHolidayBz(),tHoliday.getHolidayEndTime(),tHoliday.getHolidayStartTime(),tHoliday.getHolidayStatus());
                leaveForms.add(leaveForm);
            }
            return leaveForms;
        }
        return null;
    }

    @Override
    public String addHoliday(LeaveForm leaveForm) {
        THoliday tHoliday = new THoliday();
        Random random = new Random();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("config_name",leaveForm.getHolidayType());
        Date date = new Date(System.currentTimeMillis());
        String randomStr = "";
        for(int i = 0;i<5;i++)
            randomStr += random.nextInt(10);
        tHoliday.setHolidayNo("holiday-"+randomStr);
        tHoliday.setHolidayCreateTime(date);
        tHoliday.setHolidayUpdateTime(date);
        tHoliday.setHolidayTypeId(tConfigDao.selectOne(queryWrapper).getConfigId());
        tHoliday.setHolidayBz(leaveForm.getHolidayBz());
        tHoliday.setHolidayStatus(leaveForm.getHolidayStatus());
        tHoliday.setHolidayUserNo(leaveForm.getHolidayUserNo());
        tHoliday.setHolidayStartTime(leaveForm.getHolidayStartTime());
        tHoliday.setHolidayEndTime(leaveForm.getHolidayEndTime());

        if(tHolidayDao.insert(tHoliday) == 1)
            return "???????????????";
        else
            return "????????????!";
    }

    @Override
    public String updateHoliday(LeaveForm leaveForm,int id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("config_name",leaveForm.getHolidayType());
        int typeId = tConfigDao.selectOne(queryWrapper).getConfigId();
        queryWrapper.clear();
        THoliday tHoliday = new THoliday();
        queryWrapper.eq("holiday_id",id);
        tHoliday = tHolidayDao.selectOne(queryWrapper);
        if(tHoliday.getHolidayStatus().equals("?????????"))
            return "?????????????????????????????????????????????";
        tHoliday.setHolidayStatus(leaveForm.getHolidayStatus());
        tHoliday.setHolidayTypeId(typeId);
        tHoliday.setHolidayBz(leaveForm.getHolidayBz());
        tHoliday.setHolidayStartTime(leaveForm.getHolidayStartTime());
        tHoliday.setHolidayEndTime(leaveForm.getHolidayEndTime());
        Date date = new Date(System.currentTimeMillis());
        tHoliday.setHolidayUpdateTime(date);

        if(tHolidayDao.update(tHoliday,queryWrapper) == 1)
            return "???????????????";
        else
            return "???????????????";
    }

    @Override
    public String delHolidayById(int id) {
        QueryWrapper<THoliday> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("holiday_id",id);
        if(tHolidayDao.selectOne(queryWrapper).getHolidayStatus().equals("??????")) {
            if (tHolidayDao.deleteById(id) == 1)
                return "???????????????";
            else
                return "???????????????";
        }
        else
            return "?????????????????????????????????";

    }

    @Override
    public List<TRole> getAllRoles() {
        QueryWrapper<TRole> qw = new QueryWrapper<>();
        List<TRole> roleList = tRoleDao.selectList(qw);
        return roleList;
    }

    @Override
    public String delRoleById(int id) {
        QueryWrapper<TRole> qw = new QueryWrapper<>();
        qw.lambda().eq(TRole::getRoleId, id);
        QueryWrapper<TUser> qw2 = new QueryWrapper<>();
        qw2.lambda().eq(TUser::getUserRoleId, id);
        if(tUserDao.selectList(qw2) == null){
            if(tRoleDao.delete(qw) == 1)
                return "???????????????";
            else
                return "???????????????";
        }else{
            return "???????????????????????????????????????????????????";
        }
    }

    @Override
    public TRole getRoleById(int id) {
        QueryWrapper<TRole> qw = new QueryWrapper<>();
        qw.lambda().eq(TRole::getRoleId, id);
        TRole tRole = tRoleDao.selectOne(qw);
        return tRole;
    }

    @Override
    public String updateRole(int id, String name) {
        TRole tRole = new TRole();
        tRole.setRoleId(id);
        tRole.setRoleName(name);
        if(tRoleDao.updateById(tRole) == 1) {
            return "???????????????";
        }else {
            return "???????????????";
        }
    }

    @Override
    public List<TPermissions> getAllPermissions() {
        QueryWrapper<TPermissions> qw = new QueryWrapper<>();
        List<TPermissions> tPermissions = tPermissionsDao.selectList(qw);
        return tPermissions;
    }

    @Override
    public List<TPermissions> selectPermissionByCondition(Integer roleId, Integer menuId) {
        QueryWrapper<TPermissions> qw = new QueryWrapper<>();
        qw.lambda().eq(roleId != null, TPermissions::getPerRoleId, roleId)
                .eq(menuId != null, TPermissions::getPerMenuId, menuId);
        return tPermissionsDao.selectList(qw);
    }

    @Override
    public String addPermission(Integer roleId, Integer menuId) {
        TPermissions tPermissions = new TPermissions();
        tPermissions.setPerRoleId(roleId);
        tPermissions.setPerMenuId(menuId);
        if(tPermissionsDao.insert(tPermissions) == 1){
            return "???????????????";
        }else{
            return "???????????????";
        }

    }

    @Override
    public String delPermissionById(int id) {
        if(tPermissionsDao.deleteById(id) == 1){
            return "???????????????";
        }else{
            return "???????????????";
        }
    }

    @Override
    public String updatePermissionByCondition(int id, Integer roleId, Integer menuId) {
        TPermissions tPermissions = new TPermissions();
        tPermissions.setPerId(id);
        tPermissions.setPerMenuId(menuId);
        tPermissions.setPerRoleId(roleId);
        if(tPermissionsDao.updateById(tPermissions) == 1){
            return "???????????????";
        }else{
            return "???????????????";
        }
    }
}
