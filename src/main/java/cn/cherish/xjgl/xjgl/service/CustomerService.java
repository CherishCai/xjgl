package cn.cherish.xjgl.xjgl.service;

import cn.cherish.xjgl.xjgl.common.enums.ErrorCode;
import cn.cherish.xjgl.xjgl.common.exception.ServiceException;
import cn.cherish.xjgl.xjgl.dal.entity.Customer;
import cn.cherish.xjgl.xjgl.dal.repository.CustomerDAO;
import cn.cherish.xjgl.xjgl.dal.repository.IBaseDAO;
import cn.cherish.xjgl.xjgl.util.ObjectConvertUtil;
import cn.cherish.xjgl.xjgl.web.dto.CustomerDTO;
import cn.cherish.xjgl.xjgl.web.req.BasicSearchReq;
import cn.cherish.xjgl.xjgl.web.req.CustomerReq;
import cn.cherish.xjgl.xjgl.web.req.CustomerSearchReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CustomerService extends ABaseService<Customer, Long> {

    private final CustomerDAO customerDAO;

    @Autowired
    public CustomerService(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    protected IBaseDAO<Customer, Long> getEntityDAO() {
        return customerDAO;
    }

    public CustomerDTO findOne(Long CustomerId) {
        Customer customer = customerDAO.findOne(CustomerId);
        return customer == null ? null : ObjectConvertUtil.objectCopy(new CustomerDTO(), customer);
    }

    public Customer findByWxUserId(Long wxUserId){
        return customerDAO.findByWxUserId(wxUserId);
    }

    public Customer findByTelephone(String telephone){
        return customerDAO.findByTelephone(telephone);
    }

    public boolean exist(String telephone) {
        return customerDAO.findByTelephone(telephone) != null;
    }

    @Transactional
    public void delete(Long customerId) {
        Customer customer = customerDAO.findOne(customerId);
        if (customer == null) return;
        super.delete(customerId);
    }

    public Page<CustomerDTO> findAll(BasicSearchReq basicSearchReq, CustomerSearchReq customerSearchReq) {

        int pageNumber = basicSearchReq.getStartIndex() / basicSearchReq.getPageSize() + 1;
        PageRequest pageRequest = super.buildPageRequest(pageNumber, basicSearchReq.getPageSize());

        //除了分页条件没有特定搜索条件的，为了缓存count
        if (ObjectConvertUtil.objectFieldIsBlank(customerSearchReq)){
            log.debug("没有特定搜索条件的");
            List<Customer> customerList = customerDAO.listAllPaged(pageRequest);
            List<CustomerDTO> CustomerDTOList = customerList.stream().map(source -> {
                CustomerDTO customerDTO = new CustomerDTO();
                ObjectConvertUtil.objectCopy(customerDTO, source);
                return customerDTO;
            }).collect(Collectors.toList());

            //为了计算总数使用缓存，加快搜索速度
            Long count = getCount();
            return new PageImpl<>(CustomerDTOList, pageRequest, count);
        }

        //有了其它搜索条件
        Page<Customer> customerPage = super.findAllBySearchParams(
                buildSearchParams(customerSearchReq), pageNumber, basicSearchReq.getPageSize());

        return customerPage.map(source -> {
            CustomerDTO customerDTO = new CustomerDTO();
            ObjectConvertUtil.objectCopy(customerDTO, source);
            return customerDTO;
        });
    }

    @Transactional
    public void update(CustomerReq customerReq) {
        Customer customer = this.findById(customerReq.getId());
        if (customer == null) return;

        ObjectConvertUtil.objectCopy(customer, customerReq);
        customer.setModifiedTime(new Date());
        this.update(customer);

    }

    /**
     * 管理员手动添加会员
     * @param customerReq 会员信息
     */
    @Transactional
    public Customer save(CustomerReq customerReq) {
        //TODO 手动添加的方式待完善
        throw new ServiceException(ErrorCode.ERROR_CODE_500_001);
    }

}
