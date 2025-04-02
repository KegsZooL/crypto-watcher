package com.github.kegszool.bot.menu.service.price_snapshot;

import com.github.kegszool.bot.menu.entity.PriceSnapshotParameterInfo;
import com.github.kegszool.bot.menu.entity.PriceSnapshotProperties;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PriceSnapshotParametersRegistry {

    public Map<String, PriceSnapshotParameterInfo> createParameterMap(PriceSnapshotProperties snapshotProperties) {
        return Map.of(
                snapshotProperties.getLastPriceName(), new PriceSnapshotParameterInfo(
                        snapshotProperties.getLastPriceDescription(), snapshot -> "$" + snapshot.getLastPrice()),
                snapshotProperties.getHighestPrice24hName(), new PriceSnapshotParameterInfo(
                        snapshotProperties.getHighestPrice24hDescription(), snapshot -> "$" + snapshot.getMaxPrice24h()),
                snapshotProperties.getLowestPrice24hName(), new PriceSnapshotParameterInfo(
                        snapshotProperties.getLowestPrice24hDescription(), snapshot -> "$" + snapshot.getMinPrice24h()),
                snapshotProperties.getTradingVolumeName(), new PriceSnapshotParameterInfo(
                        snapshotProperties.getTradingVolumeDescription(), snapshot -> String.valueOf(snapshot.getTradingVolume24h())),
                snapshotProperties.getTradingVolumeCurrencyName(), new PriceSnapshotParameterInfo(
                        snapshotProperties.getTradingVolumeCurrencyDescription(), snapshot -> String.valueOf(snapshot.getTradingVolumeCurrency24h())
                )
        );
    }
}
