package max.nlp.similarity;

public class Cosine {

    public static double cosineSimilarity(int[] vec1, int[] vec2) { 
        double dp = dot_product(vec1,vec2); 
        double magnitudeA = find_magnitude(vec1); 
        double magnitudeB = find_magnitude(vec2); 
        return (dp)/(magnitudeA*magnitudeB); 
    } 

    private static double find_magnitude(int[] vec) { 
        double sum_mag=0; 
        for(int i=0;i<vec.length;i++) 
        { 
            sum_mag = sum_mag + vec[i]*vec[i]; 
        } 
        return Math.sqrt(sum_mag); 
    } 

    private static double dot_product(int[] vec1, int[] vec2) { 
        double sum=0; 
        for(int i=0;i<vec1.length;i++) 
        { 
            sum = sum + vec1[i]*vec2[i]; 
        } 
        return sum; 
    } 
}
