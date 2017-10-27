package de.hybris.platform.customerreview.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import javax.annotation.Resource;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.customerreview.constants.CustomerReviewConstants;
import java.util.List;
import javax.annotation.Resource;

/**
*A utility class that does the require task mentioned in the Problem statement
*@class CustomerReviewUtil
*/

@PropertySource("classpath:config.properties")
public class CustomerReviewUtil {

	@Resource
	private CustomerReviewService customerReviewService;

	@Value("${review.curseWords}")
	private String curseWords;
	
	/**
	 * Provide a way to get a product’s total number of customer reviews 
	 * whose ratings are within a given range (inclusive).
	 * @Function getValidNoOfReviewsForProduct
	 * @param product
	 * @return Number 
	 */
	public Integer getValidNoOfReviewsForProduct(Product product)
	{
		int counter =0;
		// fetch all the reviews for the product 
		List<CustomerReview> reviewList=getReviewsForProduct(product);
		// loop over it to check which all are in the defined range of Min and Max 
		for(CustomerReview review : reviewList)
		{
			if ((CustomerReviewConstants.getInstance().MINRATING <= rating.doubleValue()) && 
				(rating.doubleValue() <= CustomerReviewConstants.getInstance().MAXRATING))
				{
					counter++;
				}
		}
		return Integer.valueOf(counter);
	}
	
	private List<CustomerReviewModel> getReviewsForProduct(ProductModel product)
	{
		customerReviewService.getAllReviews(product);
	}
	
	
	//Task2
	public CustomerReviewModel createCustomerReview(Double rating, String headline, String comment, UserModel user, ProductModel product) throws JaloBusinessException
		{
		//validate review comments to be added 
		validateReviewComments(comment);
		
		//valid rating
		validateRating(rating);		
		
		return customerReviewService.createCustomerReview(rating, headline, comment, 
		(User)getModelService().getSource(user), (Product)getModelService().getSource(product));
		
		}
	
	private void validateReviewComments(String comment) throws JaloBusinessException
	{
		String[] curseWordList= curseWords.trim().split(",");
		if(Arrays.stream(curseWordList).parallel().anyMatch(comment::contains))
		{
		throw new JaloInvalidParameterException("Inappropriate words used in CustomerReview. Please try again", 0); // this can be added in the properties file like error.customerreview.curseWordsPresent
		}
	}
	
	private void validateRating(Double rating) throws JaloBusinessException
	{
		String[] curseWordList= curseWords.trim().split(",");
		if(rating!=null && rating.doubleValue()<0)
		{
		throw new JaloInvalidParameterException(Localization.getLocalizedString("error.customerreview.invalidrating", 0));
		}
	}
	
	
	
	
}