package store.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import store.domain.Promotion;
import store.dto.PromotionDto;

import static store.constant.Regex.SEPARATOR;
import static store.constant.Const.HEADER_LINE;
import static store.enums.PromotionIndex.NAME;
import static store.enums.PromotionIndex.BUY;
import static store.enums.PromotionIndex.GET;
import static store.enums.PromotionIndex.START_DATE;
import static store.enums.PromotionIndex.END_DATE;
import static store.enums.ListPath.PROMOTION_PATH;

public class PromotionLoaderImpl implements PromotionLoader {
	private static final PromotionLoader INSTANCE;
	
	static {
		INSTANCE = new PromotionLoaderImpl();
	}
	
	private PromotionLoaderImpl() {}
	
	public static PromotionLoader getInstance() {
		return INSTANCE;
	}
	
	@Override
	public List<Promotion> load() {
		try (BufferedReader br = new BufferedReader(new FileReader(PROMOTION_PATH.getValue()))) {
			return readPromotionList(br);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new ArrayList<>();
	}
	
	private List<Promotion> readPromotionList(final BufferedReader br) throws IOException {
		return br.lines().skip(HEADER_LINE)
				.map(this::generatePromotion)
				.collect(Collectors.toList());
	}
	
	private Promotion generatePromotion(final String line) {
		String[] data = line.split(SEPARATOR);
		
		String name = data[NAME.getValue()];
		int buy = Integer.parseInt(data[BUY.getValue()]);
		int get = Integer.parseInt(data[GET.getValue()]);
		LocalDate startDate = LocalDate.parse(data[START_DATE.getValue()]);
		LocalDate endDate = LocalDate.parse(data[END_DATE.getValue()]);
		
		return Promotion.of(PromotionDto.of(name, buy, get, startDate, endDate));
	}
}
