/**
 * Copyright 2011 Intuit Inc. All Rights Reserved
 */
package com.intuit.tank.service;

/*
 * #%L
 * JSF Support Beans
 * %%
 * Copyright (C) 2011 - 2015 Intuit Inc.
 * %%
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * #L%
 */

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.intuit.tank.dao.GroupDao;
import com.intuit.tank.dao.UserDao;
import com.intuit.tank.project.Group;
import com.intuit.tank.project.User;
import com.intuit.tank.vm.common.PasswordEncoder;
import com.intuit.tank.vm.settings.DefaultUser;
import com.intuit.tank.vm.settings.TankConfig;

/**
 * InitializeEnvironment
 * 
 * @author dangleton
 * 
 */
@Singleton
@Startup
public class InitializeEnvironment {
    private static final Logger LOG = Logger.getLogger(InitializeEnvironment.class);

    @Inject
    private TankConfig tankConfig;

    @PostConstruct
    public void initData() {
        createDefaultGroups();
        createDefaultUsers();
    }

    /**
     * @param sb
     * @return
     */
    private void createDefaultUsers() {
        UserDao dao = new UserDao();
        GroupDao groupDao = new GroupDao();
        for (DefaultUser newUser : tankConfig.getSecurityConfig().getDefaultUsers()) {
            if (dao.findByUserName(newUser.getName()) == null) {
                try {
                    User user = new User();
                    user.setName(newUser.getName());
                    user.setEmail(newUser.getEmail());
                    user.setPassword(PasswordEncoder.encodePassword(newUser.getPassword()));
                    user = dao.saveOrUpdate(user);
                    for (String g : newUser.getGroups()) {
                        Group group = groupDao.getOrCreateGroup(g);
                        user.addGroup(group);
                    }
                    for (String defaultGroup : tankConfig.getSecurityConfig().getDefaultGroups()) {
                        user.addGroup(groupDao.getOrCreateGroup(defaultGroup));
                    }
                    dao.saveOrUpdate(user);
                    LOG.info("Created user " + user.getName());
                } catch (Exception e) {
                    LOG.info("Error creating user: " + e, e);
                }
            }
        }
    }

    /**
     * @param sb
     * @return
     */
    private void createDefaultGroups() {
        GroupDao groupDao = new GroupDao();
        for (String g : tankConfig.getSecurityConfig().getGroups()) {
            try {
                Group group = groupDao.findByName(g);
                if (group == null) {
                    groupDao.saveOrUpdate(new Group(g));
                    LOG.info("Created Group " + g);
                }
            } catch (Exception e) {
                LOG.error("Error creating default Group: " + e, e);
            }
        }

    }
}
