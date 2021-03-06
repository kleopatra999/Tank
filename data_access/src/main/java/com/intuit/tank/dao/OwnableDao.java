/**
 * Copyright 2011 Intuit Inc. All Rights Reserved
 */
package com.intuit.tank.dao;

/*
 * #%L
 * Data Access
 * %%
 * Copyright (C) 2011 - 2015 Intuit Inc.
 * %%
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * #L%
 */

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.intuit.tank.project.OwnableEntity;

/**
 * OwnableDao
 * 
 * @author dangleton
 * 
 */
public abstract class OwnableDao<T_ENTITY extends OwnableEntity> extends BaseDao<T_ENTITY> {

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OwnableDao.class);

    /**
     * @param entityClass
     */
    protected OwnableDao() {
        super();
    }

    /**
     * Returns the list of entities owned by the specified user.
     * 
     * @param owner
     *            the owner.
     * @return the non null list of entities.
     */
    @Nonnull
    public List<T_ENTITY> listForOwner(@Nonnull String owner) {
        try {
            String prefix = "x";
            NamedParameter parameter = new NamedParameter(OwnableEntity.PROPERTY_CREATOR, "pId", owner);
            StringBuilder sb = new StringBuilder();
            sb.append(buildQlSelect(prefix)).append(startWhere())
                    .append(buildWhereClause(Operation.EQUALS, prefix, parameter));
            return listWithJQL(sb.toString(), parameter);
        } catch (Exception e) {
            LOG.info("No entities for owner " + owner);
        }
        return Collections.emptyList();
    }

}
