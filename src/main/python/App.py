'''from flask import Flask, jsonify, request
import pandas as pd

app = Flask(__name__)

# Sample Data (you will replace this with your actual 'liked_articles' data)
liked_articles = pd.DataFrame({
    'username': ['gig', 'gig', 'gig', 'gig'],
    'articleTitle': ['AI in Healthcare', 'Future of AI', 'Sports Analytics', 'Financial Modeling'],
    'category': ['Health', 'Technology', 'Sports', 'Finance']
})

# Recommendation logic (this is your existing function)
def recommend_articles_by_category(user):
    # Step 1: Analyze user's history to identify top categories
    user_articles = liked_articles[liked_articles["username"] == user]
    if user_articles.empty:
        return f"No viewing history found for user {user}."

    # Count articles liked in each category
    category_counts = user_articles["category"].value_counts()
    top_categories = category_counts.index[:2]  # Get the top 2 categories

    # Step 2: Filter the liked_articles by these top categories
    category_filtered_articles = liked_articles[liked_articles["category"].isin(top_categories)]

    recommendations = []

    # Step 3: Iterate through the user's liked articles and find recommendations
    for article in user_articles["articleTitle"].values:
        matching_indices = liked_articles[liked_articles["articleTitle"] == article].index

        if len(matching_indices) == 0:
            continue  # Skip if the article is not found in liked_articles

        idx = matching_indices[0]

        # Simulate cosine similarity (You can integrate your actual cosine similarity logic here)
        # For now, let's assume articles with the same category are similar.
        sim_scores = [(i, 0.9) for i in range(len(liked_articles))]  # Simulate similarity
        sim_scores = sorted(sim_scores, key=lambda x: x[1], reverse=True)

        # Recommend articles from top categories
        for i in sim_scores[1:]:  # Skip the first result (which will be the same article)
            recommended_article = liked_articles.iloc[i[0]]
            if (
                    recommended_article["category"] in top_categories
                    and recommended_article["articleTitle"] not in recommendations
            ):
                recommendations.append(recommended_article["articleTitle"])

            if len(recommendations) >= 5:  # Limit to 5 recommendations
                break

    return recommendations if recommendations else "No relevant recommendations found."

# Flask route to get recommendations
@app.route('/recommend_by_category/<user>', methods=['GET'])
def get_recommendations_by_category(user):
    recommendations = recommend_articles_by_category(user)
    return jsonify(recommendations)

if __name__ == '__main__':
    app.run(debug=True)
'''