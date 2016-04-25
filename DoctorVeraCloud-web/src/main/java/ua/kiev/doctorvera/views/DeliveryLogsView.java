package ua.kiev.doctorvera.views;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import ua.kiev.doctorvera.entities.MessageLog;
import ua.kiev.doctorvera.entities.MessageTemplate;
import ua.kiev.doctorvera.entities.TransactionLog;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.MessageLogFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.TransactionLogFacadeLocal;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static ua.kiev.doctorvera.entities.TransactionLog.Status.DELIVERED;

/**
 * Created by volodymyr.bodnar on 3/23/2016.
 */
@Named
@ViewScoped
public class DeliveryLogsView implements Serializable{
    private final static Logger LOG = Logger.getLogger(DeliveryLogsView.class.getName());

    @Inject
    private SessionParams sessionParams;

    @EJB
    private TransactionLogFacadeLocal transactionLogFacade;

    @EJB
    private MessageLogFacadeLocal messageLogFacade;

    private Users authorizedUser;

    private TransactionsLazyModel allTransactions;

    private TransactionLog.Status statusFilter;

    private MessageTemplate.Type messageTypeFilter;

    @PostConstruct
    public void init(){
        authorizedUser = sessionParams.getAuthorizedUser();
        allTransactions = new TransactionsLazyModel();
    }

    public class TransactionsLazyModel extends LazyDataModel<TransactionLog> {

        List<TransactionLog> allPaginatedFilteredLogs = new ArrayList<>();

        @Override
        public TransactionLog getRowData(String rowKey) {
            for(TransactionLog transactionLog : allPaginatedFilteredLogs) {
                if(transactionLog.getId().equals(Integer.parseInt(rowKey)))
                    return transactionLog;
            }
            return null;
        }

        @Override
        public Object getRowKey(TransactionLog transactionLog) {
            return transactionLog.getId();
        }

        @Override
        public List<TransactionLog> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
            allPaginatedFilteredLogs = transactionLogFacade.initializeLazyEntity(transactionLogFacade.findAll(first, pageSize, sortField, sortOrder, filters), sortOrder, sortField);
            setRowCount(transactionLogFacade.count(first, pageSize, filters));
            transactionLogFacade.checkTransactionStatus(allPaginatedFilteredLogs, true);
            return allPaginatedFilteredLogs;
        }
    }

    public String getTransactionRowStyle(TransactionLog transactionLog){
        final String COMPLETED = "transaction_row_completed";
        final String UNCOMPLETED = "transaction_row_uncompleted";
        final String ERROR = "transaction_row_error";

        if(transactionLog.getMessageTemplate().getType() == MessageTemplate.Type.SMS){
            switch (transactionLog.getStatus()){
                case DELIVERED:
                    return COMPLETED;
                case DELIVERY_ERROR:
                case SEND_ERROR:
                    return ERROR;
                case SENT:
                case PROGRESS:
                    return UNCOMPLETED;
            }
        }else{
            switch (transactionLog.getStatus()){
                case DELIVERED:
                case SENT:
                    return COMPLETED;
                case DELIVERY_ERROR:
                case SEND_ERROR:
                    return ERROR;
                case PROGRESS:
                    return UNCOMPLETED;
            }
        }
        return null;
    }

    public TransactionsLazyModel getAllTransactions() {
        return allTransactions;
    }

    public void setAllTransactions(TransactionsLazyModel allTransactions) {
        this.allTransactions = allTransactions;
    }

    public TransactionLog.Status getStatusFilter() {
        return statusFilter;
    }

    public void setStatusFilter(TransactionLog.Status statusFilter) {
        this.statusFilter = statusFilter;
    }
    public List<TransactionLog.Status> getTransactionStatuses(){
        return Arrays.asList(TransactionLog.Status.values());
    }

    public List<MessageLog.Status> getMessageStatuses(){
        return Arrays.asList(MessageLog.Status.values());
    }

    public List<MessageTemplate.Type> getMessageTypes(){
        return Arrays.asList(MessageTemplate.Type.values());
    }

    public MessageTemplate.Type getMessageTypeFilter() {
        return messageTypeFilter;
    }

    public void setMessageTypeFilter(MessageTemplate.Type messageTypeFilter) {
        this.messageTypeFilter = messageTypeFilter;
    }
}
