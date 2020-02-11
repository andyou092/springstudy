package com.example.study.service;

import com.example.study.model.entity.Partner;
import com.example.study.model.network.Header;
import com.example.study.model.network.request.PartnerApiRequest;
import com.example.study.model.network.response.PartnerApiResponse;
import com.example.study.repository.CategoryRepository;
import com.example.study.repository.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PartnerApiLogicService extends BaseService<PartnerApiRequest, PartnerApiResponse, Partner> {
@Autowired
    private PartnerRepository partnerRepository;
@Autowired
    private CategoryRepository categoryRepository;

    @Override
  //  @PostMapping("")
    public Header<PartnerApiResponse> create( Header<PartnerApiRequest> request) {
        System.out.println("read : "+ "들어온다");
        PartnerApiRequest body = request.getData();
        Partner partner = Partner.builder()
                .name(body.getName())
                .status(body.getStatus())
                .address(body.getAddress())
                .callCenter(body.getCallCenter())
                .partnerNumber(body.getPartnerNumber())
                .businessNumber(body.getBusinessNumber())
                .ceoName(body.getCeoName())
                .registeredAt(LocalDateTime.now())
                .unregisteredAt(body.getUnRegisteredAt())
                .category(categoryRepository.getOne(body.getCategoryId()))
                .build();

        Partner newPartner = partnerRepository.save(partner);

        return Header.OK(response(newPartner));
    }

    @Override
 //   @GetMapping("{id}")
    public Header<PartnerApiResponse> read( Long id) {
        System.out.println("read : "  + "들어온다.");
        return partnerRepository.findById(id)
                .map(this::response)//현재 클래스에서 response 메서드를 사용 하겠다는 뜻
                .map(Header::OK)
                .orElseGet(()->Header.ERROR("데이터없음"));
    }

    @Override
  //  @PutMapping("")
    public Header<PartnerApiResponse> update( Header<PartnerApiRequest> request) {
        PartnerApiRequest body = request.getData();
     return  partnerRepository.findById(body.getId())
                .map(partner -> {
                    partner
                            .setName(body.getName())
                            .setStatus(body.getStatus())
                            .setAddress(body.getAddress())
                            .setCallCenter(body.getCallCenter())
                            .setPartnerNumber(body.getPartnerNumber())
                            .setBusinessNumber(body.getBusinessNumber())
                            .setCeoName(body.getCeoName())
                            .setRegisteredAt(LocalDateTime.now())
                            .setUnregisteredAt(body.getUnRegisteredAt())
                            .setCategory(categoryRepository.getOne(body.getCategoryId()));
                    return partner;
                })
                .map(changePartner -> partnerRepository.save(changePartner))
                .map((newPartner-> response(newPartner)))
                .map(Header::OK)
                .orElseGet(()-> Header.ERROR("update : 데이터 없음"));

    }

    @Override
//    @DeleteMapping("{id}")
    public Header delete( Long id) {
        System.out.println("delete들어온다");
        return  partnerRepository.findById(id)
                .map(partner -> {
                    partnerRepository.delete(partner);
                    return Header.OK();
                })
                .orElseGet(()->Header.ERROR("데이터 없음"));

    }

    public PartnerApiResponse response(Partner partner){

        PartnerApiResponse partnerApiResponse = PartnerApiResponse.builder()
                .id((partner.getId()))
                .name(partner.getName())
                .status(partner.getStatus())
                .address(partner.getAddress())
                .callCenter(partner.getCallCenter())
                .partnerNumber(partner.getPartnerNumber())
                .businessNumber(partner.getBusinessNumber())
                .ceoName(partner.getCeoName())
                .registeredAt(LocalDateTime.now())
                .unRegisteredAt(partner.getUnregisteredAt())
                .categoryId(partner.getCategory().getId())
                .build();

        return partnerApiResponse;
    }


    public Header<List<PartnerApiResponse>> search(Pageable pageable) {

        Page<Partner> partners = partnerRepository.findAll(pageable);

        List<PartnerApiResponse> partnerApiResponsesList = partners.stream()
                .map(partner->response(partner))
                .collect(Collectors.toList());

        //List<UserApiResponse>
        //Header<List<UserApiResponse>>
        return Header.OK(partnerApiResponsesList);
    }
    //페이징을 위해서

}
