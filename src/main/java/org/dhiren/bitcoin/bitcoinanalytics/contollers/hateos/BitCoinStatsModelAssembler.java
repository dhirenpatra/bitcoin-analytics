package org.dhiren.bitcoin.bitcoinanalytics.contollers.hateos;

import org.dhiren.bitcoin.bitcoinanalytics.contollers.BitcoinController;
import org.dhiren.bitcoin.bitcoinanalytics.models.BitCoinStats;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BitCoinStatsModelAssembler implements RepresentationModelAssembler<BitCoinStats, EntityModel<BitCoinStats>> {

    @Override
    public EntityModel<BitCoinStats> toModel(BitCoinStats bitCoinStats) {

        EntityModel<BitCoinStats> itemModel = EntityModel.of(bitCoinStats);

        itemModel.add(linkTo(methodOn(BitcoinController.class)
                        .convertCurrency(bitCoinStats.getMaxValue(), bitCoinStats.getMinValue(),
                                bitCoinStats.getCurrency(), "INR")).withRel("converter"));

        // Adding link to another method with request parameters
        // itemModel.add(linkTo(methodOn(BitcoinController.class).getItemsByName(item.getName())).withRel("items-by-name"));

        return itemModel;
    }
}