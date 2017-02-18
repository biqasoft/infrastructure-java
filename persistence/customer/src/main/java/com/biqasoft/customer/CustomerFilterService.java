/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.customer;

import com.biqasoft.persistence.base.DateService;
import com.biqasoft.entity.constants.SYSTEM_FIELDS_CONST;
import com.biqasoft.entity.customer.Customer;
import com.biqasoft.entity.filters.CustomerFilter;
import com.biqasoft.entity.format.BiqaPaginationResultList;
import com.biqasoft.persistence.base.BiqaObjectFilterService;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CustomerFilterService {

    private final BiqaObjectFilterService biqaObjectFilterService;
    private final DateService dateService;

    public CustomerFilterService(BiqaObjectFilterService biqaObjectFilterService, DateService dateService) {
        this.biqaObjectFilterService = biqaObjectFilterService;
        this.dateService = dateService;
    }

    public BiqaPaginationResultList<Customer> getCustomersByFilterForDomain(CustomerFilter filter, MongoOperations ops) {
        Criteria criteria = this.biqaObjectFilterService.getCriteriaFromAbstractBuilder(filter);
        Query query = this.biqaObjectFilterService.getQueryFromFilter(filter, criteria);

        if (filter.isCustomer()) criteria.and("customer").is(true);
        if (filter.isActive()) criteria.and("active").is(true);
        if (filter.isLead()) criteria.and("lead").is(true);
        if (filter.isImportant()) criteria.and("important").is(true);
        if (filter.getLeadGenMethodId() != null && !filter.getLeadGenMethodId().equals(SYSTEM_FIELDS_CONST.ANY))
            criteria.and("leadGenMethod").is(filter.getLeadGenMethodId());

        if (filter.getLeadGenProjectId() != null && !filter.getLeadGenProjectId().equals(SYSTEM_FIELDS_CONST.ANY))
            criteria.and("leadGenProject").is(filter.getLeadGenProjectId());

        if (filter.getSalesFunnelStatusID() != null && !filter.getSalesFunnelStatusID().equals(SYSTEM_FIELDS_CONST.ANY)) {
            ObjectId objectId = new ObjectId(filter.getSalesFunnelStatusID());
            criteria.and("salesFunnelStatus._id").is(objectId);
        }

        //
        //  filters by customer overview data
        //

        // only more then priority filter
        if (filter.isUseActiveTaskNumberMoreThan() && !filter.isUseActiveTaskNumberLessThan())
            criteria.and("customerOverview.activeTaskNumber").gte(filter.getActiveTaskNumberMoreThan());
        // only less then priority filter
        if (filter.isUseActiveTaskNumberLessThan() && !filter.isUseActiveTaskNumberMoreThan())
            criteria.and("customerOverview.activeTaskNumber").lte(filter.getActiveTaskNumberLessThan());
        //  less then and more then priority filter together
        if (filter.isUseActiveTaskNumberLessThan() && filter.isUseActiveTaskNumberMoreThan())
            criteria.and("customerOverview.activeTaskNumber").lte(filter.getActiveTaskNumberLessThan()).gte(filter.getActiveTaskNumberMoreThan());


        // only more then priority filter
        if (filter.isUseDealsAmountMoreThan() && !filter.isUseDealsAmountLessThan())
            criteria.and("customerOverview.dealsAmount").gte(filter.getDealsAmountMoreThan());
        // only less then priority filter
        if (filter.isUseDealsAmountLessThan() && !filter.isUseDealsAmountMoreThan())
            criteria.and("customerOverview.dealsAmount").lte(filter.getDealsAmountLessThan());
        //  less then and more then priority filter together
        if (filter.isUseDealsAmountMoreThan() && filter.isUseDealsAmountLessThan())
            criteria.and("customerOverview.dealsAmount").lte(filter.getDealsAmountLessThan()).gte(filter.getDealsAmountMoreThan());


        // only more then priority filter
        if (filter.isUseOpportunityAmountMoreThan() && !filter.isUseOpportunityAmountLessThan())
            criteria.and("customerOverview.opportunityAmount").gte(filter.getOpportunityAmountMoreThan());
        // only less then priority filter
        if (filter.isUseOpportunityAmountLessThan() && !filter.isUseOpportunityAmountMoreThan())
            criteria.and("customerOverview.opportunityAmount").lte(filter.getOpportunityAmountLessThan());
        //  less then and more then priority filter together
        if (filter.isUseOpportunityAmountMoreThan() && filter.isUseOpportunityAmountLessThan())
            criteria.and("customerOverview.opportunityAmount").lte(filter.getOpportunityAmountLessThan()).gte(filter.getOpportunityAmountMoreThan());


        // only more then priority filter
        if (filter.isUseOpportunityNumberMoreThan() && !filter.isUseOpportunityNumberLessThan())
            criteria.and("customerOverview.opportunityNumber").gte(filter.getOpportunityNumberMoreThan());
        // only less then priority filter
        if (filter.isUseOpportunityNumberLessThan() && !filter.isUseOpportunityNumberMoreThan())
            criteria.and("customerOverview.opportunityNumber").lte(filter.getOpportunityNumberLessThan());
        //  less then and more then priority filter together
        if (filter.isUseOpportunityNumberMoreThan() && filter.isUseOpportunityNumberLessThan())
            criteria.and("customerOverview.opportunityNumber").lte(filter.isUseOpportunityNumberLessThan()).gte(filter.isUseOpportunityNumberMoreThan());


        // only more then priority filter
        if (filter.isUseDealsNumberMoreThan() && !filter.isUseDealsNumberLessThan())
            criteria.and("customerOverview.dealsNumber").gte(filter.getDealsNumberMoreThan());
        // only less then priority filter
        if (filter.isUseDealsNumberLessThan() && !filter.isUseDealsNumberMoreThan())
            criteria.and("customerOverview.dealsNumber").lte(filter.getDealsNumberLessThan());
        //  less then and more then priority filter together
        if (filter.isUseDealsNumberMoreThan() && filter.isUseDealsNumberLessThan())
            criteria.and("customerOverview.dealsNumber").lte(filter.getDealsNumberLessThan()).gte(filter.getDealsNumberMoreThan());

        
        
        // created date
        if (filter.isUseRelativeLastDealDateFrom() && !filter.isUseRelativeLastDealDateTo()) {
            criteria.and("customerOverview.lastDealDate").gte( dateService.parseDateExpression( filter.getRelativeLastDealDateFrom() ) );
        }

        if (!filter.isUseRelativeLastDealDateFrom() && filter.isUseRelativeLastDealDateTo()) {
            criteria.and("customerOverview.lastDealDate").lte( dateService.parseDateExpression( filter.getRelativeLastDealDateTo() ) );
        }

        if (filter.isUseRelativeLastDealDateFrom() && filter.isUseRelativeLastDealDateTo()) {
            Date dateFrom =  dateService.parseDateExpression( filter.getRelativeLastDealDateFrom() )  ;
            Date dateTo =  dateService.parseDateExpression( filter.getRelativeLastDealDateTo() )  ;
            criteria.and("customerOverview.lastDealDate").lte( dateTo  ).gte( dateFrom );
        }

        ///////////////////////

        // created last opportunity date
        if (filter.isUseRelativeLastOpportunityDateFrom() && !filter.isUseRelativeLastOpportunityDateTo()) {
            criteria.and("customerOverview.lastOpportunityDate").gte( dateService.parseDateExpression( filter.getRelativeLastOpportunityDateFrom() ) );
        }

        if (!filter.isUseRelativeLastOpportunityDateFrom() && filter.isUseRelativeLastOpportunityDateTo()) {
            criteria.and("customerOverview.lastOpportunityDate").lte( dateService.parseDateExpression( filter.getRelativeLastOpportunityDateTo() ) );
        }

        if (filter.isUseRelativeLastOpportunityDateFrom() && filter.isUseRelativeLastOpportunityDateTo()) {
            Date dateFrom =  dateService.parseDateExpression( filter.getRelativeLastOpportunityDateFrom() )  ;
            Date dateTo =  dateService.parseDateExpression( filter.getRelativeLastOpportunityDateTo() )  ;
            criteria.and("customerOverview.lastOpportunityDate").lte( dateTo  ).gte( dateFrom );
        }
        

        if (filter.isUseStaticSegments())
            criteria.and("staticSegmentsIDs").in(filter.getStaticSegmentsIDs());

        if (filter.isUseCustomerIDsList()) criteria.and("id").in(filter.getCustomerIDsList());

        if (filter.isUseResponsiblesManagersList())
            criteria.and("responsibleManagerID").in(filter.getResponsiblesManagersList());

        return biqaObjectFilterService.getPaginationResultList(filter, criteria, query, Customer.class, ops);
    }

}
